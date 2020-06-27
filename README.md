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
