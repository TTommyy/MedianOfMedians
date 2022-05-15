//Tomasz Koczar - 5
//Zadanie z kursu "Metody Programowania"
//15.05.2022AD

/*Problem selekcji:
    Dane wejsciowe:
    S - zbior z powtorzeniami o mocy n
    k - liczba, 1 <= k <= n
    Wyjscie:
    k-ty element ciagu uporzadkowanego
    elementow S.
 */

/*Idea rozwiazania:
Algorytm Bluma-Floyda-Pratta-Rivesta-Tarjana
zwany rowniez "mediana median" badz "magicznymi piatkami".
*/


/*Importy*/
import java.util.Scanner;//Obsluga wyjscia/wejscia (IO).


/*-----------Porzadek---------------*/
interface Porzadek{
    boolean wPorzadku(int pierwsza, int druga);
}

class Rosnacy implements Porzadek{
    @Override
    public boolean wPorzadku(int pierwsza, int druga){
        return  pierwsza < druga;
    }

}
/*-----------Selekcja----------------*/
class Selekcja{
    /**Dzielenie Lomuto*/
    private int dziel(int[] tablica,int lewy, int prawy, Porzadek porzadek){
        int i = lewy-1, pivot = tablica[prawy], tymczasowy;
        for(int j = lewy; j < prawy; j++){
            if(porzadek.wPorzadku(tablica[j], pivot)) {
                i++;
                tymczasowy = tablica[i];
                tablica[i] = tablica[j];
                tablica[j] = tymczasowy;
            }
        }
        tablica[prawy] = tablica[i+1];
        tablica[i+1] = pivot;
        return i+1;
    }
}


/**----------Klasa obslugujaca zestaw danych---------*/
final class Zestaw{
    /*------------Pola-----------*/
    /*Tablica reprezentuje zbior
    Zapytania dotycza k-tego elementu
     */
    final private int dlugoscTablicy,iloscZapytan;
    final private int[] tablica,zapytania;
    /*-----------Metody----------*/
    /**Konstruktor*/
    public Zestaw(Scanner sc){
        /*Pobranie danych wejsciowych z wejscia*/
        dlugoscTablicy = sc.nextInt();
        tablica = new int[dlugoscTablicy];
        for(int i =0 ; i<dlugoscTablicy ; i++){
            tablica[i] = sc.nextInt();
        }
        iloscZapytan = sc.nextInt();
        zapytania = new int[iloscZapytan];
        for (int i = 0; i < iloscZapytan; i++) {
            zapytania[i] = sc.nextInt();
        }
    }

}


/**------------------Program-------------------------*/
public class Source {

    /**Wejscie do programu*/
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        int iloscZestawow = sc.nextInt();
        Zestaw zestaw;
        for (int i = 0; i < iloscZestawow; i++) {
            zestaw = new Zestaw(sc);
        }
    }
}
