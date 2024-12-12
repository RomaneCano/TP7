package champollion;

import java.util.Date;

public class Intervention {
    private Date debut;
    private int duree;
    private boolean annulee;
    private TypeIntervention type;
    private Salle salle;
    private UE ue;

    public Intervention(Date debut, int duree, TypeIntervention type, Salle salle, UE ue) {
        this.debut = debut;
        this.duree = duree;
        this.annulee = false; // Default value
        this.type = type;
        this.salle = salle;
        this.ue = ue;
    }

    public Date getDebut() {
        return debut;
    }

    public void setDebut(Date debut) {
        this.debut = debut;
    }

    public int getDuree() {
        return duree;
    }
    public void setDuree(int duree) {
        this.duree = duree;
    }

    public boolean isAnnulee() {
        return annulee;
    }

    public void setAnnulee(boolean annulee) {
        this.annulee = annulee;
    }

    public TypeIntervention getType() {
        return type;
    }

    public void setType(TypeIntervention type) {
        this.type = type;
    }

    public Salle getSalle() {
        return salle;
    }

    public void setSalle(Salle salle) {
        this.salle = salle;
    }

    public UE getUe() {
        return ue;
    }

    public void setUe(UE ue) {
        this.ue = ue;
    }
}

