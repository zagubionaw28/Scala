# Laboratorium 14

## Zadanie 14.1

Używając aktorów stwórz symulację bitwy dwóch rodów. Każdy ród posiada `Zamek`, a w nim 100 `Obrońców`, którzy ostrzeliwują zamek przeciwnika z łuków. Żeby walka była uczciwa rozkaz do ostrzału wydaje (pojedyncza) `SiłaWyższa`. Rozkaz ten trafia do `Zamku`, a następnie rozsyłany jest wśród `Obrońców`, którzy w danej chwili bronią jego murów. Zasady walki stanowią:

- `Zamki` rozpoczynają bitwę ze 100 `Obrońcami`, których nazwiemy „aktywnymi”;
- Co sekundę `SiłaWyższa` daje `Zamkom` sygnał do strzelania;
- `Zamek`, po otrzymaniu sygnału od `SiłyWyższej`, wydaje rozkaz oddania strzału wszystkim „aktywnym obrońcom”;
- Strzały trafiają w `Zamek` przeciwnika i w losowy sposób spadają na jego `Obrońców` (losowanie „ofiary” ostrzału powinno odbywać się na poziomie ostrzelanego `Zamku`)
- `Obrońca`, na którego spada strzała traci życie;
- Żeby być zdolnym do walki `Zamek` musi mieć przynajmniej jednego aktywnego `Obrońcę`;
- Gdy wszyscy `Obrońcy` danego `Zamku` zginą, ogłasza on, że przegrał bitwę – kończy to symulację (`system.terminate`).

### Uwaga 1

To, że strzelanie odbywa się co sekundę nie jest ważne – może to być 0.01 sekundy żeby bitwa trwała krócej. Ważne żeby każdy `Obrońca` miał taką samą „szybkostrzelność”.

__Podpowiedź__: Do stworzenia rozwiązania użyj szablonu projektu zawartego w pliku `Laboratorium_14.zip`.

### Uwaga 2

Zauważ, że planista może wysyłać komunikaty jedynie do __aktora__. Jak zorganizować zatem wysyłanie `Strzał`-ów do dwóch zamków? A co byłoby, gdyby odbiorców było jeszcze więcej? Potencjalne rozwiązanie kryje się w materiałach z __wykładu 13__.

### Propozycja

Sprawdź ile sygnałów do strzelania zostaje wysłanych zanim któryś z zamków wygra bitwę. Zastanów się nad wprowadzeniem elementów losowych, które mogłyby wydłużyć rozgrywkę.
