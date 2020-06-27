# tpia-ep2-mdp
Planning Topics of Artificial Intelligence - EP2 - Markovian Decision Process

---

## Execução do Projeto

### Compilar o projeto

* Usuários de _Windows_, execute o código:

        javac src/PD.java; javac src/DD.java; javac src/MDPAction.java; javac src/MDPState.java; javac src/Problem.java; javac src/Main.java
    
* Usuários do _Linux_ ou _MacOS_, execute o código:

        javac src/PD.java && javac src/DD.java && javac src/MDPAction.java && javac src/MDPState.java && javac src/Problem.java && javac src/Main.java

### Executar o projeto 

**AVISO: Para os usuários de Windows, recomenda-se não utilizar o parâmetro de imprimir o *grid*, pois o Windows não interpreta Unicode diretamente, e são necessárias configurações extras para que ele possa funcionar, e mesmo assim não é garantido por não ser padrão do console do mesmo.**

Para arquivos do _FixedGoalInitialState_ ou _RandomGoalInitialState_:

    java src/Main [-f | -r] [-vi | -pi] [1-10] [-p]

Para arquivos do _RunningExample_:

    java src/Main -ex [-vi | -pi] [-p]

##### Parâmetros:

###### `[-f | -r]`

* `-f` para os arquivos do _FixedGoalInitialState_
* `-r` para os arquivos do _RandomGoalInitialState_

###### `[-vi | -pi]`

* `-iv` para o algoritmo _Value Iteration_
* `-ip` para o algoritmo _Policy Iteration_

###### `[1-10]`

* O número do arquivo, de 1 a 10

###### `[p]`

* Parâmetro OPCIONAL, caso queira imprimir o grid

#### Executar todos e salvar em arquivos

Value Iteration:
* VI Fixed from 1 to 10 with output: 
`java src/Main -f -iv 1 -p > output-vi-fixed-1.txt && java src/Main -f -iv 2 -p > output-vi-fixed-2.txt && java src/Main -f -iv 3 -p > output-vi-fixed-3.txt && java src/Main -f -iv 4 -p > output-vi-fixed-4.txt && java src/Main -f -iv 5 -p > output-vi-fixed-5.txt && java src/Main -f -iv 6 -p > output-vi-fixed-6.txt && java src/Main -f -iv 7 -p > output-vi-fixed-7.txt && java src/Main -f -iv 8 -p > output-vi-fixed-8.txt && java src/Main -f -iv 9 -p > output-vi-fixed-9.txt && java src/Main -f -iv 10 -p > output-vi-fixed-10.txt`
* VI Random from 1 to 10 with output: `java src/Main -r -iv 1 -p > output-vi-random-1.txt && java src/Main -r -iv 2 -p > output-vi-random-2.txt && java src/Main -r -iv 3 -p > output-vi-random-3.txt && java src/Main -r -iv 4 -p > output-vi-random-4.txt && java src/Main -r -iv 5 -p > output-vi-random-5.txt && java src/Main -r -iv 6 -p > output-vi-random-6.txt && java src/Main -r -iv 7 -p > output-vi-random-7.txt && java src/Main -r -iv 8 -p > output-vi-random-8.txt && java src/Main -r -iv 9 -p > output-vi-random-9.txt && java src/Main -r -iv 10 -p > output-vi-random-10.txt`

Policy Iteration:
* PI Fixed from 1 to 10 with output: `java src/Main -f -ip 1 -p > output-pi-fixed-1.txt && java src/Main -f -ip 2 -p > output-pi-fixed-2.txt && java src/Main -f -ip 3 -p > output-pi-fixed-3.txt && java src/Main -f -ip 4 -p > output-pi-fixed-4.txt && java src/Main -f -ip 5 -p > output-pi-fixed-5.txt && java src/Main -f -ip 6 -p > output-pi-fixed-6.txt && java src/Main -f -ip 7 -p > output-pi-fixed-7.txt && java src/Main -f -ip 8 -p > output-pi-fixed-8.txt && java src/Main -f -ip 9 -p > output-pi-fixed-9.txt && java src/Main -f -ip 10 -p > output-pi-fixed-10.txt`
* PI Random from 1 to 10 with output: `java src/Main -r -ip 1 -p > output-pi-random-1.txt && java src/Main -r -ip 2 -p > output-pi-random-2.txt && java src/Main -r -ip 3 -p > output-pi-random-3.txt && java src/Main -r -ip 4 -p > output-pi-random-4.txt && java src/Main -r -ip 5 -p > output-pi-random-5.txt && java src/Main -r -ip 6 -p > output-pi-random-6.txt && java src/Main -r -ip 7 -p > output-pi-random-7.txt && java src/Main -r -ip 8 -p > output-pi-random-8.txt && java src/Main -r -ip 9 -p > output-pi-random-9.txt && java src/Main -r -ip 10 -p > output-pi-random-10.txt`
---

## Português - Brasileiro

### Padrão do arquivo '.net'

O arquivo tem:

1. Os estados separados por vírgulas. 

2. As transições para cada ação definidas da seguinte forma:

        action nome_da_ação
            estado_corrente estado_sucessor probabilidade_da_ação descartar
        end_action

3. O custo de cada par estado ação.

4. O estado inicial.

5. O estado meta.

6. Um grid que é apenas para visualização. No grid:

    1. Parede
    2. Estado Inicial
    3. Estado Final
    4. Marcação que indica que há uma parede do lado

---

## English

### '.net' file pattern

The file has:

1. States separeted by comma. 

2. The transitions for each action defined as follows: 

        action action_name
            current_state successor_state probability_of_action discard
        end_action

3. The cost of each pair composed by state and action.

4. Initial state.

5. Goal state.

6. A grid just for visualization. In the grid:

    1. Wall
    2. Initial state
    3. Final state
    4. Markup that indicates if there is a wall at the side
