package com.capgemini.spacex.capsules;

import com.capgemini.spacex.capsules.client.models.CapsuleResponse;
import com.capgemini.spacex.capsules.repositories.models.Capsule;
import com.capgemini.spacex.capsules.repositories.models.Mission;

import java.time.OffsetDateTime;
import java.util.Arrays;
import java.util.List;
import java.util.Set;

public class TestUtils {
    private TestUtils() {
        // Prevent someone trying to instantiate this class
    }

    public static List<Capsule> createMockCapsuleList() {
        var capsule1 = new Capsule();
        capsule1.setCapsuleId("1");
        capsule1.setCapsuleSerial("C102");
        capsule1.setMissions(createMockMissions(capsule1));
        capsule1.setLandings(1);
        capsule1.setOriginalLaunch(OffsetDateTime.now());
        capsule1.setReuseCount(1);
        capsule1.setStatus("Active");


        var capsule2 = new Capsule();
        capsule2.setCapsuleId("2");
        capsule2.setCapsuleSerial("C103");
        capsule2.setMissions(Set.of(createMockMission(capsule2)));
        capsule2.setLandings(1);
        capsule2.setOriginalLaunch(OffsetDateTime.now());
        capsule2.setReuseCount(1);
        capsule2.setStatus("Active");

        return Arrays.asList(capsule1, capsule2);
    }

    public static Capsule createMockCapsule() {
        var capsule = new Capsule();
        capsule.setCapsuleId("1");
        capsule.setCapsuleSerial("C103");
        capsule.setMissions(createMockMissions(capsule));
        capsule.setLandings(4);
        capsule.setOriginalLaunch(OffsetDateTime.now());
        capsule.setReuseCount(2);
        capsule.setStatus("Active");

        return capsule;
    }

    public static CapsuleResponse createMockClientCapsuleResponse() {
        var capsuleResponse = new CapsuleResponse();
        capsuleResponse.setCapsuleId("1");
        capsuleResponse.setCapsuleSerial("C103");
        capsuleResponse.setLandings(4);
        capsuleResponse.setReuseCount(2);
        capsuleResponse.setOriginalLaunch(OffsetDateTime.now());
        capsuleResponse.setMissions(createMockClientMissions());
        capsuleResponse.setStatus("Active");

        return capsuleResponse;
    }

    private static List<com.capgemini.spacex.capsules.client.models.Mission> createMockClientMissions() {
        var mission1 = new com.capgemini.spacex.capsules.client.models.Mission();
        mission1.setName("Test Mission");
        mission1.setFlight(1);

        var mission2 = new com.capgemini.spacex.capsules.client.models.Mission();
        mission2.setName("Test Mission");
        mission2.setFlight(2);

        return Arrays.asList(mission1, mission2);
    }

    private static Set<Mission> createMockMissions(Capsule capsule) {
        var mission1 = new Mission();
        mission1.setName("Test Mission");
        mission1.setFlight(1);

        var mission2 = new Mission();
        mission2.setName("Test Mission");
        mission2.setFlight(2);

        mission1.setCapsule(capsule);
        mission2.setCapsule(capsule);

        return Set.of(mission1, mission2);
    }

    private static Mission createMockMission(Capsule capsule) {
        var mission3 = new Mission();
        mission3.setName("Test Mission 3");
        mission3.setFlight(1);

        mission3.setCapsule(capsule);

        return mission3;
    }
}
