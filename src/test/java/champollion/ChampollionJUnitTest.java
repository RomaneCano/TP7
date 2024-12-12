package champollion;

import org.junit.jupiter.api.*;
import static org.junit.jupiter.api.Assertions.*;

import java.util.Date;

public class ChampollionJUnitTest {
    Enseignant untel;
    UE uml, java;
    Salle salle;

    @BeforeEach
    public void setUp() {
        untel = new Enseignant("untel", "untel@gmail.com");
        uml = new UE("UML");
        java = new UE("Programmation en java");
        salle = new Salle("Salle 101", 30);
    }

    @Test
    public void testNouvelEnseignantSansService() {
        assertEquals(0, untel.heuresPrevues(), "Un nouvel enseignant doit avoir 0 heures prévues");
    }

    @Test
    public void testAjouteHeures() {
        // 10h TD pour UML
        untel.ajouteEnseignement(uml, 0, 10, 0);

        assertEquals(10, untel.heuresPrevuesPourUE(uml),
            "L'enseignant doit maintenant avoir 10 heures prévues pour l'UE 'uml'");

        // 20h TD pour UML
        untel.ajouteEnseignement(uml, 0, 20, 0);

        assertEquals(10 + 20, untel.heuresPrevuesPourUE(uml),
            "L'enseignant doit maintenant avoir 30 heures prévues pour l'UE 'uml'");
    }

    @Test
    public void testHeuresPrevuesTotal() {
        untel.ajouteEnseignement(uml, 10, 15, 5);
        untel.ajouteEnseignement(java, 5, 10, 5);

        double total = (10*1.5) + (15*1) + (5*0.75) + (5*1.5) + (10*1) + (5*0.75);
        assertEquals((int) Math.round(total), untel.heuresPrevues(), "Les heures prévues totales doivent être correctes.");
    }

    @Test
    public void testHeuresPrevuesPourUEAucuneUE() {
        assertEquals(0, untel.heuresPrevuesPourUE(uml),
            "Sans enseignement ajouté, les heures prévues pour une UE doivent être 0.");
    }

    @Test
    public void testHeuresPrevuesPourUENonExistant() {
        untel.ajouteEnseignement(java, 5, 10, 5);
        assertEquals(0, untel.heuresPrevuesPourUE(uml),
            "Les heures prévues pour une UE non existante doivent être 0.");
    }

    @Test
    public void testResteAPlanifierAucuneIntervention() {
        untel.ajouteEnseignement(uml, 5, 10, 5);
        assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.CM),
            "Le reste à planifier pour un CM doit correspondre au service prévu.");
        assertEquals(10, untel.resteAPlanifier(uml, TypeIntervention.TD),
            "Le reste à planifier pour un TD doit correspondre au service prévu.");
        assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.TP),
            "Le reste à planifier pour un TP doit correspondre au service prévu.");
    }

    @Test
    public void testResteAPlanifierAvecInterventions() {
        untel.ajouteEnseignement(uml, 5, 10, 5);
        untel.ajouteIntervention(new Intervention(new Date(), 3, TypeIntervention.CM, salle, uml));
        untel.ajouteIntervention(new Intervention(new Date(), 5, TypeIntervention.TD, salle, uml));

        assertEquals(2, untel.resteAPlanifier(uml, TypeIntervention.CM),
            "Le reste à planifier doit être réduit par les interventions CM.");
        assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.TD),
            "Le reste à planifier doit être réduit par les interventions TD.");
        assertEquals(5, untel.resteAPlanifier(uml, TypeIntervention.TP),
            "Le reste à planifier pour TP doit rester inchangé.");
    }

    @Test
    public void testAjouteInterventionValide() {
        untel.ajouteEnseignement(uml, 10, 15, 5);

        Intervention intervention = new Intervention(new Date(), 5, TypeIntervention.TD, salle, uml);
        untel.ajouteIntervention(intervention);

        assertEquals(10, untel.resteAPlanifier(uml, TypeIntervention.TD),
            "Les heures restantes après planification doivent être correctes.");
    }

    @Test
    public void testAjouteInterventionInvalide() {
        untel.ajouteEnseignement(uml, 2, 2, 2);

        Intervention intervention = new Intervention(new Date(), 3, TypeIntervention.TD, salle, uml);

        assertThrows(IllegalArgumentException.class, () ->
                untel.ajouteIntervention(intervention),
            "Une exception doit être levée si l'intervention dépasse le service prévu.");
    }

    @Test
    public void testAjouteInterventionDurationsInvalides() {
        untel.ajouteEnseignement(uml, 5, 10, 5);

        assertThrows(IllegalArgumentException.class, () ->
                untel.ajouteIntervention(new Intervention(new Date(), -3, TypeIntervention.CM, salle, uml)),
            "Une exception doit être levée pour une durée négative.");
        assertThrows(IllegalArgumentException.class, () ->
                untel.ajouteIntervention(new Intervention(new Date(), 0, TypeIntervention.TD, salle, uml)),
            "Une exception doit être levée pour une durée nulle.");
    }

    @Test
    public void testEstEnSousServiceCasLimite() {
        untel.ajouteEnseignement(uml, 128, 0, 0); // 192 heures équivalent TD
        assertFalse(untel.estEnSousService(),
            "Un enseignant avec exactement 192 heures prévues n'est pas en sous-service.");

        untel.ajouteEnseignement(java, 0, 1, 0);
        assertFalse(untel.estEnSousService(),
            "Un enseignant avec plus de 192 heures prévues n'est pas en sous-service.");

        untel = new Enseignant("nouvel", "nouvel@gmail.com"); // Reset
        untel.ajouteEnseignement(uml, 127, 0, 0); // Moins de 192 heures équivalent TD
        assertTrue(untel.estEnSousService(),
            "Un enseignant avec moins de 192 heures prévues est en sous-service.");
    }
}
