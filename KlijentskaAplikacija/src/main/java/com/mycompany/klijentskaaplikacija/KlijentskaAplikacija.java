package com.mycompany.klijentskaaplikacija;

import endpoints.Podsistem1EP;
import endpoints.Podsistem2EP;
import endpoints.Podsistem3EP;
import entities.Filijala;
import entities.Komitent;
import entities.Mesto;
import entities.Racun;
import entities.Transakcija;
import java.io.IOException;
import java.util.List;
import java.util.Scanner;
import java.util.logging.Level;
import java.util.logging.Logger;
import javax.ws.rs.core.Response;
import retrofit2.Call;
import retrofit2.Retrofit;
import retrofit2.converter.gson.GsonConverterFactory;

/**
 *
 * @author Martin
 */
public class KlijentskaAplikacija {

    public static void main(String[] args) {
        String menu = "Meni:\n\n1. Kreiranje mesta\n"
                + "2. Kreiranje filijale u mestu\n3. Kreiranje komitenta\n"
                + "4. Promena sedista za zadatog komitenta\n5. Otvaranje racuna\n"
                + "6. Zatvaranje racuna\n7. Kreiranje transakcije koja je prenos sume sa jednog racuna na drugi racun\n"
                + "8. Kreiranje transakcije koja je uplata novca na racun\n9. Kreiranje transakcije koja je isplata novca sa racuna\n"
                + "10. Dohvatanje svih mesta\n11. Dohvatanje svih filijala\n"
                + "12. Dohvatanje svih komitenata\n13. Dohvatanje svih racuna za komitenta\n"
                + "14. Dohvatanje svih transakcija za racun\n15. Dohvatanje svih podataka iz rezervne kopije\n"
                + "16. Dohvatanje razlike u podacima u originalnim podacima i u rezervnoj kopiji\n\n0. Izlaz";

        Scanner scanner = new Scanner(System.in);
        int choice = 0;
        while (true) {
            System.out.println(menu);
            try {
                choice = scanner.nextInt();
            } catch (Exception ex) {
                System.out.println("Uneti cifru");
                continue;
            }
            if (choice == 1) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/mesto/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdMes:");
                int idMes = scanner.nextInt();
                System.out.println("Uneti posBroj:");
                String posBroj = scanner.next();
                System.out.println("Uneti naziv:");
                String naziv = scanner.next();

                Mesto mesto = new Mesto(idMes, naziv, posBroj);

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    service.createMesto(mesto).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 2) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/filijala/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdFil:");
                int idFil = scanner.nextInt();
                System.out.println("Uneti adresu:");
                String adresa = scanner.next();
                System.out.println("Uneti naziv:");
                String naziv = scanner.next();
                System.out.println("Uneti IdMes:");
                int idMes = scanner.nextInt();

                Filijala filijala = new Filijala(idFil, naziv, adresa, idMes);

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    service.createFilijala(filijala).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 3) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/komitent/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdKom:");
                int idKom = scanner.nextInt();
                System.out.println("Uneti adresu:");
                String adresa = scanner.next();
                System.out.println("Uneti naziv:");
                String naziv = scanner.next();
                System.out.println("Uneti IdMes:");
                int idMes = scanner.nextInt();

                Komitent komitent = new Komitent(idKom, naziv, adresa, idMes);

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    service.createKomitent(komitent).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 4) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/komitent{idKom}/{idMes}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdKom:");
                int idKom = scanner.nextInt();
                System.out.println("Uneti IdMes:");
                int idMes = scanner.nextInt();

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    service.changeMestoForKomitent(idKom, idMes).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 5) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/racun/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdRac:");
                int idRac = scanner.nextInt();
                System.out.println("Uneti datum i vreme:");
                String datumVreme = scanner.next();
                System.out.println("Uneti dozvoljeni minus:");
                int dozvMinus = scanner.nextInt();
                System.out.println("Uneti IdFil:");
                int idFil = scanner.nextInt();
                System.out.println("Uneti IdKom:");
                int idKom = scanner.nextInt();

                Racun racun = new Racun(idRac, datumVreme, 0, dozvMinus, "Aktivan", 0, idFil, idKom);

                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    service.createRacun(racun).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 6) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/racun{idRac}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdRac:");
                int idRac = scanner.nextInt();
                
                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    service.closeRacun(idRac).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 7) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/prenos/{IdRac}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdTra:");
                int idTra = scanner.nextInt();
                System.out.println("Uneti datum i vreme:");
                String datumVreme = scanner.next();
                System.out.println("Uneti iznos:");
                int iznos = scanner.nextInt();
                System.out.println("Uneti svrhu:");
                String svrha = scanner.next();
                System.out.println("Uneti IdRac:");
                int idRac = scanner.nextInt();
                System.out.println("Uneti IdRac na koji se prenosi novac:");
                int naRac = scanner.nextInt();

                Transakcija transakcija = new Transakcija(idTra, datumVreme, iznos, 0, svrha, idRac);

                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    service.createPrenos(transakcija, naRac).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 8) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/uplata/{IdFil}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdTra:");
                int idTra = scanner.nextInt();
                System.out.println("Uneti datum i vreme:");
                String datumVreme = scanner.next();
                System.out.println("Uneti iznos:");
                int iznos = scanner.nextInt();
                System.out.println("Uneti svrhu:");
                String svrha = scanner.next();
                System.out.println("Uneti IdRac:");
                int idRac = scanner.nextInt();
                System.out.println("Uneti IdFil:");
                int idFil = scanner.nextInt();

                Transakcija transakcija = new Transakcija(idTra, datumVreme, iznos, 0, svrha, idRac);

                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    service.createUplata(transakcija, idFil).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 9) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/isplata/{IdFil}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdTra:");
                int idTra = scanner.nextInt();
                System.out.println("Uneti datum i vreme:");
                String datumVreme = scanner.next();
                System.out.println("Uneti iznos:");
                int iznos = scanner.nextInt();
                System.out.println("Uneti svrhu:");
                String svrha = scanner.next();
                System.out.println("Uneti IdRac:");
                int idRac = scanner.nextInt();
                System.out.println("Uneti IdFil:");
                int idFil = scanner.nextInt();

                Transakcija transakcija = new Transakcija(idTra, datumVreme, iznos, 0, svrha, idRac);

                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    service.createIsplata(transakcija, idFil).execute();
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
                System.out.println("Uspesno");
            } else if (choice == 10) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/mesto/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    List<Mesto> body = service.getMesta().execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 11) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/filijala/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    List<Filijala> body = service.getFilijale().execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 12) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem1/komitent/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Podsistem1EP service = retrofit.create(Podsistem1EP.class);
                try {
                    List<Komitent> body = service.getKomitenti().execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 13) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/racun/{idKom}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdKom:");
                int idKom = scanner.nextInt();

                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    List<Racun> body = service.getRacunForKomitent(idKom).execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 14) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem2/transakcija/{idRac}/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                System.out.println("Uneti IdRac:");
                int idRac = scanner.nextInt();

                Podsistem2EP service = retrofit.create(Podsistem2EP.class);
                try {
                    List<Transakcija> body = service.getTransakcijaForRacun(idRac).execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 15) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem3/sve/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Podsistem3EP service = retrofit.create(Podsistem3EP.class);
                try {
                    List<Object> body = service.getSve().execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 16) {
                Retrofit retrofit = new Retrofit.Builder()
                        .baseUrl("http://localhost:8080/CentralniServer/resources/podsistem3/razlika/")
                        .addConverterFactory(GsonConverterFactory.create())
                        .build();

                Podsistem3EP service = retrofit.create(Podsistem3EP.class);
                try {
                    List<Object> body = service.getRazlika().execute().body();
                    if (body != null) {
                        System.out.println(body.toString());
                    }
                } catch (IOException ex) {
                    //Logger.getLogger(KlijentskaAplikacija.class.getName()).log(Level.SEVERE, null, ex);
                }
            } else if (choice == 0) {
                break;
            }
        }
        scanner.close();
    }

}
