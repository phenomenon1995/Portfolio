"""
Tic Tac Toe Player
"""

import math
from copy import deepcopy

X = "X"
O = "O"
EMPTY = None
tally = 0 
value = { X: 1, O: -1, EMPTY: 0}
'''
test_board1 = [[X, EMPTY, O],
            [O, X, EMPTY],
            [EMPTY, EMPTY, O]]
test_board2 = [[X, EMPTY, O],
            [EMPTY, X, O],
            [X, EMPTY, EMPTY]]
test_board3 = [[EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY]]
test_board4 = [[X, X, EMPTY],
               [O, X, O],
                [X, O, O ]]
test_boards = [test_board1, test_board2, test_board3, test_board4]
'''
def initial_state():
    """
    Returns starting state of the board.
    """
    return [[EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY],
            [EMPTY, EMPTY, EMPTY]]

def player(board):
    """
    Returns player who has the next turn on a board.
    """
    
    if terminal(board) == True:
        return None
    else: #if there is an even number of moves, it's X turn
        X = 0
        O = 0
        for r in board:
            for c in r:
                if c == "X":
                    X += 1
                elif c == "O":
                    O += 1
        filled = X + O
        if filled % 2 == 0:
            return "X"
        else:
            return "O"
    raise NotImplementedError

def actions(board):
    """
    Returns set of all possible actions (i, j) available on the board.
    """
    
    if terminal(board) == True:
        return "GAME OVER"
    else: #if the cell empty, it is a valid move
        x = 0 
        y = 0 
        actions = []

        for r in board:
            x += 1
            for c in r:
                y += 1
                if c == EMPTY:
                    actions.append((x-1,y-1))
            y = 0
        return set(actions)
    raise NotImplementedError

def result(board, action):
    """
    Returns the board that results from making move (i, j) on the board.
    """
    if board[action[0]][action[1]] != EMPTY: #space taken
        raise ValueError("Invalid Move")
    elif 0 > action[0] < 2 or 0 > action[1] < 2: #space out of bounds
        raise ValueError("Invalid Move") 
    else:
        new_board = deepcopy(board) #don't wanna change the orginal object
        if player(board) == X:
            new_board[action[0]][action[1]] =  X
        if player(board) == O:
            new_board[action[0]][action[1]] =  O
    
    return new_board

    raise NotImplementedError

def winner(board):
    """
    Returns the winner of the game, if there is one.
    """
    if terminal(board) == True:
        if tally == 3:
            return X
        elif tally == -3:
            return O
    if terminal(board) == False:
        return None

def terminal(board):
    """
    Returns True if game is over, False otherwise.
    """
    global tally
    filled = 0

    for r in board:#check horizontal win
        tally = 0
        for c in r: 
            if c != EMPTY:
                filled += 1
                if c == X:
                    tally += 1
                if c == O:
                    tally -= 1
        if abs(tally) == 3:
#            print("horizontal win")
            return True
    for x in range(3):#check vertical win
        tally = 0
        for y in range(3):
            if board[y][x] == X:
                tally += 1
            elif board[y][x] == O:
                tally -= 1
        if abs(tally) == 3:
#            print("vertical win")
            return True
    if board[1][1] != EMPTY:#check diagonal win
        tally = value[board[0][0]] + value[board[1][1]] + value[board[2][2]]
        if abs(tally) == 3:
#            print("Left Diagonal Win")
            return True
        else:
            tally = value[board[2][0]] + value[board[1][1]] + value[board[0][2]]
            if abs(tally) == 3:    
    #            print("right diagonal win")
                return True
    if filled == 9:#check draw
#        print("Tied")
        return True

    return False            
    raise NotImplementedError

def utility(board):
    """
    Returns 1 if X has won the game, -1 if O has won, 0 otherwise.
    """
    if winner(board) == X: 
        return 1
    elif winner(board) == O:
        return -1
    else:
        return 0
    raise NotImplementedError

def minimax(board):
    """
    Returns the optimal action for the current player on the board.
    """
    if terminal(board) == True:
        return None
    else:   
        options = []
        if player(board) == X:
            v = -math.inf #starting point
            for action in actions(board):
                u = max(v, minValue(result(board, action))) #pick highest value that adversary returns
                if u > v: #alpha-beta prune, don't add if higher value already exists
                    v = u 
                    options.append((action, v))
            return max(options, key= lambda x: x[1])[0] #sort by largest and gimme the first one
        if player(board) == O:
            v = math.inf
            for action in actions(board):
                u = min(v, maxValue(result(board, action)))
                if u < v:
                    v = u
                    options.append((action, v))
            return min(options, key= lambda x: x[1])[0]
    raise NotImplementedError

def maxValue(board):
    if terminal(board) == True:
        return utility(board)
    v = -math.inf
    for action in actions(board):
        v = max(v, minValue(result(board, action)))
        #maybe alpha-beta pruning goes here instead? 
    return v

def minValue(board):
    if terminal(board) == True:
        return utility(board)
    v = math.inf
    for action in actions(board):
        v = min(v, maxValue(result(board, action)))
    return v
