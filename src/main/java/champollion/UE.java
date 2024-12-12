package champollion;

import java.util.ArrayList;
import java.util.List;

public class UE {
    private final String myIntitule;
    private List<Intervention> interventions;

    public UE(String intitule) {
        myIntitule = intitule;
        this.interventions = new ArrayList<>();
    }

    public String getIntitule() {
        return myIntitule;
    }

    public void ajouterIntervention(Intervention intervention) {
        this.interventions.add(intervention);
    }

    public List<Intervention> getInterventions() {
        return interventions;
    }
}
