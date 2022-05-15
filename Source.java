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
        return  pierwsza <= druga;
    }

}
/*-----------Selekcja----------------*/
class Selekcja{
    /*-------- Baza rekrencji---------*/
    private int p;

    /**Konstruktor*/
    public Selekcja(int p){ this.p = p; }

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

    /**Sortowanie prosty wyborem*/
    public void sortuj(int[] tablica, int lewy, int prawy, Porzadek porzadek){
        int i, pivot , j, temp;
        for(i = lewy ; i < prawy; i++){
            pivot = i;
            for(j = i+1; j<prawy+1;j++){
                if(porzadek.wPorzadku(tablica[j],tablica[pivot])) pivot = j;
            }
            temp = tablica[pivot];
            tablica[pivot] = tablica[i];
            tablica[i] = temp;
        }
    }

    public int selekcja(int[] tablica, int lewy, int prawy,
                        int k, Porzadek porzadek){

        /*Warunek stopu*/
        if(prawy - lewy < p){
            sortuj(tablica, lewy, prawy, porzadek);
            return tablica[k];//Zwroc k-ty element
        }

        /*Podziel zbior na 5 elementowe pozbiory,
        posortuj osobno*/
        int i;
        for (i = 0; i < tablica.length+5; i++) {
            sortuj(tablica, i, i +5, porzadek);
        }
        sortuj(tablica, i+5, tablica.length-1,porzadek);

        /*Wyznacz zbior median pozbiorow*/

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

    static public void display(int[] tablica){
        for (int i = 0; i < tablica.length; i++) {
            System.out.print(tablica[i] + " ");
        }
        System.out.print("\n");
    }
    /**Wejscie do programu*/
    public static Scanner sc = new Scanner(System.in);

    public static void main(String[] args){
        Selekcja selekcja = new Selekcja();
        Rosnacy rosnacy = new Rosnacy();

        int iloscZestawow = sc.nextInt();
        Zestaw zestaw;
        for (int i = 0; i < iloscZestawow; i++) {
            zestaw = new Zestaw(sc);
        }

        /*int[] t = {2,7,4,8,5,9};
        selekcja.sortuj(t,0, t.length -1,rosnacy);
        display(t);*/

    }
}
