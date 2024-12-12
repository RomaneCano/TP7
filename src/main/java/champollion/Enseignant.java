package champollion;

import java.util.ArrayList;
import java.util.Date;
import java.util.List;

/**
 * Un enseignant est caractérisé par les informations suivantes : son nom, son adresse email, et son service prévu,
 * et son emploi du temps.
 */
public class Enseignant extends Personne {
    private List<ServicePrevu> servicesPrevus;
    private List<Intervention> interventionsPlanifiees;

    public Enseignant(String nom, String email) {
        super(nom, email);
        this.servicesPrevus = new ArrayList<>();
        this.interventionsPlanifiees = new ArrayList<>();
    }

    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant en "heures équivalent TD" Pour le calcul : 1 heure
     * de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure de TP vaut 0,75h
     * "équivalent TD"
     *
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     */
    public double heuresPrevues() {
        double total = 0.0;
        for (ServicePrevu service : servicesPrevus) {
            total += service.getVolumeCM() * 1.5 + service.getVolumeTD() + service.getVolumeTP() * 0.75;
        }
        return total;
    }


    /**
     * Calcule le nombre total d'heures prévues pour cet enseignant dans l'UE spécifiée en "heures équivalent TD" Pour
     * le calcul : 1 heure de cours magistral vaut 1,5 h "équivalent TD" 1 heure de TD vaut 1h "équivalent TD" 1 heure
     * de TP vaut 0,75h "équivalent TD"
     *
     * @param ue l'UE concernée
     * @return le nombre total d'heures "équivalent TD" prévues pour cet enseignant, arrondi à l'entier le plus proche
     */
    public double heuresPrevuesPourUE(UE ue) {
        double total = 0.0;
        for (ServicePrevu service : servicesPrevus) {
            if (service.getUe().equals(ue)) {
                total += service.getVolumeCM() * 1.5 + service.getVolumeTD() + service.getVolumeTP() * 0.75;
            }
        }
        return total;
    }


    /**
     * Ajoute un enseignement au service prévu pour cet enseignant
     *
     * @param ue l'UE concernée
     * @param volumeCM le volume d'heures de cours magistral
     * @param volumeTD le volume d'heures de TD
     * @param volumeTP le volume d'heures de TP
     */
    public void ajouteEnseignement(UE ue, int volumeCM, int volumeTD, int volumeTP) {
        servicesPrevus.add(new ServicePrevu(ue, volumeCM, volumeTD, volumeTP));
    }
    /**
     * Ajoute une intervention planifiée pour cet enseignant
     *
     * @param intervention l'intervention à ajouter
     * @throws IllegalArgumentException si l'intervention dépasse le service prévu
     */
    public void ajouteIntervention(Intervention intervention) throws IllegalArgumentException {
        if (intervention.getDuree() <= 0) {
            throw new IllegalArgumentException("La durée de l'intervention doit être strictement positive.");
        }

        int heuresPlanifiees = 0;
        for (Intervention i : interventionsPlanifiees) {
            if (i.getUe().equals(intervention.getUe()) && i.getType().equals(intervention.getType())) {
                heuresPlanifiees += i.getDuree();
            }
        }

        int heuresRestantes = resteAPlanifier(intervention.getUe(), intervention.getType());
        if (heuresPlanifiees + intervention.getDuree() > heuresRestantes) {
            throw new IllegalArgumentException("Intervention dépasse le service prévu pour l'UE et le type d'intervention.");
        }

        interventionsPlanifiees.add(intervention);
    }

    /**
     * Calcule la différence entre le service prévu et les interventions planifiées pour une UE et un type d'intervention donnés.
     *
     * @param ue l'UE concernée
     * @param type le type d'intervention
     * @return le volume horaire restant à planifier
     */
    public int resteAPlanifier(UE ue, TypeIntervention type) {
        int heuresPrevues = 0;
        int heuresPlanifiees = 0;

        for (ServicePrevu service : servicesPrevus) {
            if (service.getUe().equals(ue)) {
                switch (type) {
                    case CM -> heuresPrevues += service.getVolumeCM();
                    case TD -> heuresPrevues += service.getVolumeTD();
                    case TP -> heuresPrevues += service.getVolumeTP();
                }
            }
        }

        for (Intervention intervention : interventionsPlanifiees) {
            if (intervention.getUe().equals(ue) && intervention.getType().equals(type)) {
                heuresPlanifiees += intervention.getDuree();
            }
        }

        return heuresPrevues - heuresPlanifiees;
    }

    /**
     * Vérifie si un enseignant est en sous-service (moins de 192 heures prévues).
     *
     * @return true si l'enseignant est en sous-service, false sinon
     */
    public boolean estEnSousService() {
        return heuresPrevues() < 192;
    }
}
