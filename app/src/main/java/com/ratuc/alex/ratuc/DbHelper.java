package com.ratuc.alex.ratuc;

import java.util.List;

import io.realm.Realm;
import io.realm.RealmConfiguration;



class DbHelper {      // clasa ajutatoare pentru salvarea listei de linii in baza de date, pt salvare folosesc Realm
    private static final int DB_VERSION = 1;

    private static RealmConfiguration realmConfiguration;

    private static Realm getRealm() {
        if (realmConfiguration == null) {                    // verifica daca exista configuratie sau nu
            Realm.init(RatucApp.getInstance());              // initializam realm

            realmConfiguration = new RealmConfiguration.Builder()      // cream o configuratie
                    .deleteRealmIfMigrationNeeded()
                    .schemaVersion(DB_VERSION)
                    .build();
        }

        return Realm.getInstance(realmConfiguration);
    }

    static List<Line> getLines() {    //metoda care returneaza lista de linii salvate, e statica ca sa o putem apela in main activity fara instanta
        Realm realm = getRealm();   //apela in main activity fara instanta

        return realm.copyFromRealm(realm.where(Line.class).findAll());
    }

    private static void clearLines(){
        Realm realm = getRealm();
        realm.beginTransaction();
        realm.delete(Line.class);
        realm.commitTransaction();
        realm.close();
    }

    static void saveLines(final List<Line> schedules) {        //metoda pentru salvarea listei de linii in baza de date
        clearLines();

        Realm realm = getRealm();
        realm.beginTransaction();
        realm.copyToRealmOrUpdate(schedules);
        realm.commitTransaction();
        realm.close();
    }
}
