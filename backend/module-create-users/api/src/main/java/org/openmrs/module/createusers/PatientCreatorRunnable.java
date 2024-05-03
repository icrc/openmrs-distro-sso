package org.openmrs.module.createusers;

import org.openmrs.*;
import org.openmrs.api.PatientService;
import org.openmrs.api.VisitService;
import org.openmrs.api.context.Context;
import org.slf4j.Logger;
import org.slf4j.LoggerFactory;

import java.time.LocalDateTime;
import java.util.List;

import static org.openmrs.module.referencedemodata.Randomizer.randomBetween;
import static org.openmrs.module.referencedemodata.Randomizer.shouldRandomEventOccur;
import static org.openmrs.module.referencedemodata.ReferenceDemoDataUtils.toLocalDateTime;

/**
 * Create visits in a dedicated thread.
 */
public class PatientCreatorRunnable implements Runnable {
  Logger log = LoggerFactory.getLogger(PatientCreatorRunnable.class);
  private final Integer patientId;
  private final PatientService patientService;
  private final Generators generators;
  private final List<VisitType> visitTypes;
  private final VisitService visitService;

  public PatientCreatorRunnable(Integer patientId, PatientService patientService, Generators generators, List<VisitType> visitTypes, VisitService visitService) {
    this.patientId = patientId;
    this.patientService = patientService;
    this.generators = generators;
    this.visitTypes = visitTypes;
    this.visitService = visitService;
  }

  @Override
  public void run() {
    Context.openSessionWithCurrentUser();
    try {
      log.info("create visits for the patient {}", patientId);
      boolean isInProgram = false;
      Patient patient = Context.getPatientService().getPatient(patientId);
      log.info("create visits for the patient {}", patient.getId());
      int visitCount = randomBetween(1, Math.max(Math.round(patient.getAge() / 10.5f), 1));

      log.info("create {} visits for the patient {}", visitCount, patientId);
      Location visitLocation = patient.getPatientIdentifier().getLocation();
      log.info("create {} visits for the patient {} on location {}", visitCount, patientId, visitLocation.getName());

      Visit lastVisit = null;
      for (int i = 0; i < visitCount; i++) {
        boolean shortVisit = shouldRandomEventOccur(0.8);
        lastVisit = generators.visitGenerator.createDemoVisit(patient, visitTypes, visitLocation, shortVisit, lastVisit, visitCount - (i + 1));


        // about 1/3 patients in their first 2 visits will be registered as part of a program
        if (!isInProgram && i < 2) {
          if (shouldRandomEventOccur(.33)) {
            isInProgram = true;
            generators.programGenerator.createDemoPatientProgram(patient, lastVisit.getStartDatetime());
          }
        }

        // we want to ensure that all patients are relatively current, so basically should've had a
        // visit in the past 6 months
        if (i == visitCount - 1) {
          LocalDateTime now = LocalDateTime.now();
          LocalDateTime lastVisitDateTime = toLocalDateTime(lastVisit.getStopDatetime());
          int yearsMissing = now.getYear() - lastVisitDateTime.getYear();
          if (yearsMissing > 0) {
            visitCount += yearsMissing;
          } else {
            //                  ICRC to have an active visit at least.
            lastVisit.setStopDatetime(null);
            visitService.saveVisit(lastVisit);
          }
        }
      }

      log.info("created {} visits for patient {}", visitCount, patient.getPatientIdentifier());
      try {
        Context.flushSession();
      } catch (Exception exception) {
        log.error("cant create visits", exception);
      }
    } catch (Exception e) {
      log.error("cant create visits", e);
    } finally {
      Context.closeSessionWithCurrentUser();
    }
  }

}
