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
Jest oparty na metodzie dziel i zwyciezaj.
Wyznaczmy 5 elementowe podzbiory, oraz mediane ich median, nastepnie sprawdzmy
czy poszukwany element jest od niej wiekszy, badz mniejszy.
Konutnujuemy rekurencyjnie szukanie w adekwanym podzbiorze.
*/

/*Ocena zlozonosci
    Dla n < p, T(n) = c1*n, gdzie c1 jest pewna stala. Dzieje sie tak powiewaz wystrczy posortowac
    n-elementowa tablice i zwrocic k-ty element.
    Dla n >= p, T(n) = T(n/5) + T( 3/4 n) + c2 * n, powiwaz mamy jedno podzadanie wielkosci n/5(Mediana median)
    oraz jedno wielkosci conajwyzej 3/4n (Selekcja w pierwsztm badz trzecim podzbiorze) oraz  kosztem liniowym
    dzielimy zbior na 5 elmentowe podzbiory.
    Indukcjinie mozna udowadnic ,ze  T(n) <= 20c*n = |O|(n), gdzie c = max(c1,c2).

 */

/*Test jawny zwrca:
1 1
2 2
3 3
2 3
5 5
1 3
3 4
4 4
1 1
10 1
0 brak
20 brak
11 brak
Tak jak oczekiwalsmy
*/


/*Importy*/
import java.util.Scanner;//Obsluga wyjscia/wejscia (IO).


/*-----------Porzadek---------------*/
interface Porzadek{
    boolean wPorzadku(int pierwsza, int druga);
}

final class Rosnacy implements Porzadek{
    @Override
    public boolean wPorzadku(int pierwsza, int druga){
        return  pierwsza < druga;
    }

}
/*-----------Selekcja----------------*/
final class Selekcja{
    /*-------- Baza rekrencji---------*/
    private final int p;

    /**Konstruktor*/
    public Selekcja(int p){ this.p = p; }

    /**Zamienia w tablicy i-ty element z j-tym*/
    private void podmien(int[] tablica, int i, int j){
        int temp = tablica[i];
        tablica[i] = tablica[j];
        tablica[j] = temp;
    }

    /**Dzielenie na wieksze oraz mniejsze od pivota*/
    private int dziel(int[] tablica,int lewy, int prawy, int pivot, Porzadek porzadek){
        int i,j;

        /*Znajdz i przestaw pivot na prawy koniec*/
        for (i = lewy; i <prawy ; i++) {
            if(tablica[i] == pivot)break;
        }
        podmien(tablica,i,prawy);


        /*Standorowe dzielenie jak u Lomuto*/
        i = lewy;
        for( j = lewy; j < prawy; j++){
            if(porzadek.wPorzadku(tablica[j], pivot)) {
               podmien(tablica, i,j);
                i++;
            }
        }
        podmien(tablica, i, prawy);
        return i;
    }

    /**Sortowanie przez proste wstawianie*/
    private void sortuj(int[] tablica, int lewy, int prawy, Porzadek porzadek){
        int i,j,temp;                                                           //Do iterowania
        for(i = lewy + 1; i <= prawy ; i++){
            temp = tablica[i];                                                  //"Karta" do wstwienia
            j = i-1;
            while( j >= lewy && porzadek.wPorzadku(temp,tablica[j])){           //Szukamy dla niej miejsca
                tablica[j+1] = tablica[j];                                      //Przesuwamy posortowane karty
                j--;
            }
            tablica[j+1] = temp;                                                //Wstaw karte za pierwsza mniejsza
        }
    }

    /**Zwraca mediane z posortowanej listy*/
    private int mediana(int[] tablica, int lewy, int moc){
        return tablica[lewy + (moc)/2];
    }

    /**Glowna funkcja*/
    public int selekcja(int[] tablica, int lewy, int prawy,
                        int k, Porzadek porzadek ){
        //System.out.println("Lewy: " + lewy + " Prawy: " + prawy);

        /*Obliczamy moc podanego zbioru*/
        int mocZbioru = prawy - lewy + 1;

        /*Warunek stopu, rozwiazujemy bez rekursji*/
        if(mocZbioru < p){
            sortuj(tablica, lewy, prawy, porzadek);     //Posortuj
            return tablica[lewy + k];                   //Zwroc k-ty element
        }

        /*Podziel zbior na 5 elementowe podzbiory,
        posortuj osobno,wyznacz zbior median pozbiorow*/
        int i,iloscPodzbiorow = (mocZbioru+4)/5;

        /*Zbior median = Q*/
        int[] Q = new int[iloscPodzbiorow];

        for (i = 0; i < mocZbioru/5; i++) {
            sortuj(tablica, lewy+(i*5), lewy + (i*5) + 4, porzadek);
            Q[i] = mediana(tablica,lewy+(i*5),5);

        }
        if(i*5 < mocZbioru) {
            sortuj(tablica, lewy + (i*5), prawy, porzadek);
            Q[i] = mediana(tablica,lewy+(i*5),mocZbioru%5);
        }



        /*Wyznacz rekurencyjnie mediane median*/
        int M = selekcja(Q, 0 , iloscPodzbiorow-1, iloscPodzbiorow/2 ,porzadek);

        /*Podziel na 3 podzbiory*/
        int podzial = dziel(tablica,lewy,prawy,M,porzadek);


        /*I decuduj*/
        /*Moc pierwszgo zbioru = podzial - lewy
          Moc drugiego podzbioru = 1
          Moc trzeciego podzbioru = prawy - podzial
         */
        int moc1 = podzial - lewy;
        if(moc1 == k-1) return M; //Akurat, mamy nasz k-ty element!
        /*K-ty element zawiera sie w pierwszym podzbiorze*/
        if(moc1 > k-1) return selekcja(tablica,lewy,podzial-1,k,porzadek);
        /*K-ty element zawiera sie w trzecim podzbiorze*/
        return selekcja(tablica,podzial+1,prawy, k - moc1 - 1 , porzadek);

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

    public void wykonajSwaPowinnosc(Selekcja selekcja, Porzadek porzadek){
        for (int i = 0; i < iloscZapytan; i++) {
            if(zapytania[i] >= 1 && zapytania[i] <= dlugoscTablicy ){
                int s = selekcja.selekcja(tablica,0,dlugoscTablicy-1,zapytania[i] - 1, porzadek);
                System.out.println( zapytania[i] + " " +  s);
            }else {
                System.out.println( zapytania[i] + " " +  "brak");
            }
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
        Selekcja selekcja = new Selekcja(50);
        Rosnacy rosnacy = new Rosnacy();

        int iloscZestawow = sc.nextInt();
        Zestaw zestaw;
        for (int i = 0; i < iloscZestawow; i++) {
            zestaw = new Zestaw(sc);
            zestaw.wykonajSwaPowinnosc(selekcja,rosnacy);
        }


    }
}
