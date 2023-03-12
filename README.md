# Tema 1 - Pocris Sabin - 322CB

## `QuizGenerator`

- `QuizGenerator()`
  - in functie de `args`, incarca in memorie clasa de care acesta are nevoie
  - ex: daca trebuie sa fac o intrebare noua, voi incarca clasa `QuestionsDatabase`
- `start()`
  - are rol de `main()`
  - in functie de ce actiune trebuie sa efectueze(`add`,`get`,`delete`,`update`), va transmite mesajul specific actiunii instantei `database`
- `exit()`
  - in cazul in care o actiune ce modifica continutul unui database se realizeaza cu succes, atunci aceasta metoda este apelata pentru a salva obiectul intr-un fisier

## `Loader`

- obiectele ce urmeaza sa fie salvate, folosesc interfata `Serializable`
- TL;DR
  - incarca in memorie obiectul din fisier (`loadObject()`)
  - scrie in fisier obiectul din memorie(`saveObject()`)
  - fiecare tip de baza de date are path-ul prestabilit(ex: `dataFolder`, `usersDatabase` fields)

## `Database`

Dupa multe rescrieri, am ajuns la concluzia ca cele 3 principale tipuri de date(`Question`,`User`,`Quiz`) pot fi stocate intr-o "baza de date", fiindca cerintele se rezuma la cateva actiuni: `add`,`get`,`delete`,`update`, urmand sa pot extinde usor programul daca ar fi sa se adauge o noua cerinta (ex: `Fill in the Blanks`).

Astfel, aceste actiuni sunt adaugate in clasa parinte, `Database`, urmand ca fiecare clasa copil sa implementeze aceste actiuni.

In unele cazuri (ex: `loginSuccessful`), am ales sa fac o functionalitate comuna pentru ca toate functiile au nevoie de aceasta.

## `Parser`

- contine metode necesare parsarii inputului

## `Utility`

- contine metode necesare ce nu tine de parsare dar nu isi au rost intr-o clasa principala precum: `User`, `Question`, etc.

## `Factory`

Am observat in tema un design pattern: `Factory Pattern`, clasa `Factory` avand acest rol.

## `UserDatabase`, `QuestionDatabase`, `QuizDatabase`

- mostenesc clasa `Database`
- sunt baze de date pentru tipul de date caracteristic: `Question`,`User`,`Quiz`

## `Authenticator`

- Stocheaza intr-un `HashMap` user-ul si parola
- Ideea din spate a fost sa separ datele de logare de bazele de date pentru a putea fi folosite si de alte clase.


## BONUS

### Cazuri limita

- Sa calculez un top al userilor in functie de punctaj
- Hash-uirea parolelor
- Posibilitatea de a trimite un Quiz doar unui grup/unei persoane

### Refactor comenzi/raspunsuri

- As scrie intr-un fisier un raspuns mai detaliat: ce a gresit la Quiz user-ul respectiv, cate persoane au mai gresit, etc.
- Posibilitatea de a afisa un hint
- Quiz-ul sa contina si ce nivel de dificultate au intrebarile
  - Intrebarile bonus sa nu scada din punctajul total
  - Intrebarile grele sa valoreze mai mult
