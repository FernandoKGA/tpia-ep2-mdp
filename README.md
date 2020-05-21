# tpia-ep2-mdp
Planning Topics of Artificial Intelligence - EP2 - Markovian Decision Process

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
