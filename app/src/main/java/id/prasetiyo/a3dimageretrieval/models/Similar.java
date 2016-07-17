package id.prasetiyo.a3dimageretrieval.models;

import java.io.Serializable;

/**
 * Created by aoktox on 01/07/16.
 */
public class Similar implements Serializable{
    private int id;
    private int jarak;

    public Similar(int jarak, int id) {
        this.jarak = jarak;
        this.id = id;
    }

    public int getId() {
        return id;
    }

    public void setId(int id) {
        this.id = id;
    }

    public int getJarak() {
        return jarak;
    }

    public void setJarak(int jarak) {
        this.jarak = jarak;
    }
}
