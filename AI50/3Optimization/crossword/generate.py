import sys

from crossword import *


class CrosswordCreator():

    def __init__(self, crossword):
        """
        Create new CSP crossword generate.
        """
        self.crossword = crossword
        self.domains = {
            var: self.crossword.words.copy()
            for var in self.crossword.variables
        }

    def letter_grid(self, assignment):
        """
        Return 2D array representing a given assignment.
        """
        letters = [
            [None for _ in range(self.crossword.width)]
            for _ in range(self.crossword.height)
        ]
        for variable, word in assignment.items():
            direction = variable.direction
            for k in range(len(word)):
                i = variable.i + (k if direction == Variable.DOWN else 0)
                j = variable.j + (k if direction == Variable.ACROSS else 0)
                letters[i][j] = word[k]
        return letters

    def print(self, assignment):
        """
        Print crossword assignment to the terminal.
        """
        letters = self.letter_grid(assignment)
        for i in range(self.crossword.height):
            for j in range(self.crossword.width):
                if self.crossword.structure[i][j]:
                    print(letters[i][j] or " ", end="")
                else:
                    print("█", end="")
            print()

    def save(self, assignment, filename):
        """
        Save crossword assignment to an image file.
        """
        from PIL import Image, ImageDraw, ImageFont
        cell_size = 100
        cell_border = 2
        interior_size = cell_size - 2 * cell_border
        letters = self.letter_grid(assignment)

        # Create a blank canvas
        img = Image.new(
            "RGBA",
            (self.crossword.width * cell_size,
             self.crossword.height * cell_size),
            "black"
        )
        font = ImageFont.truetype("assets/fonts/OpenSans-Regular.ttf", 80)
        draw = ImageDraw.Draw(img)

        for i in range(self.crossword.height):
            for j in range(self.crossword.width):

                rect = [
                    (j * cell_size + cell_border,
                     i * cell_size + cell_border),
                    ((j + 1) * cell_size - cell_border,
                     (i + 1) * cell_size - cell_border)
                ]
                if self.crossword.structure[i][j]:
                    draw.rectangle(rect, fill="white")
                    if letters[i][j]:
                        _, _, w, h = draw.textbbox((0, 0), letters[i][j], font=font)
                        draw.text(
                            (rect[0][0] + ((interior_size - w) / 2),
                             rect[0][1] + ((interior_size - h) / 2) - 10),
                            letters[i][j], fill="black", font=font
                        )

        img.save(filename)

    def solve(self):
        """
        Enforce node and arc consistency, and then solve the CSP.
        """
        self.enforce_node_consistency()
        self.ac3()
        return self.backtrack(dict())

    def enforce_node_consistency(self):
        """
        Update `self.domains` such that each variable is node-consistent.
        (Remove any values that are inconsistent with a variable's unary
         constraints; in this case, the length of the word.)
        """
        for var in self.domains:
            bad_words = []
            for word in self.domains[var]:
                if len(word) != var.length:
                    bad_words.append(word)

            for word in bad_words:
                self.domains[var].remove(word)
 
        return None
        raise NotImplementedError

    def revise(self, x, y):
        """
        Make variable `x` arc consistent with variable `y`.
        To do so, remove values from `self.domains[x]` for which there is no
        possible corresponding value for `y` in `self.domains[y]`.

        Return True if a revision was made to the domain of `x`; return
        False if no revision was made.
        """
        overlap = self.crossword.overlaps[x,y]
        modified = False

        if overlap:
            xDomain = self.domains[x]
            yDomain = self.domains[y]
            bad_words = []
            for xWord in xDomain:
                options = 0 
                for yWord in yDomain:
                    if yWord[overlap[1]] == xWord[overlap[0]]:
                        options += 1
                if options == 0:
                    bad_words.append(xWord)
            if bad_words:
                for word in bad_words:
                    xDomain.remove(word)
                modified = True

        
        return modified
        raise NotImplementedError

    def ac3(self, arcs=None):
        """
        Update `self.domains` such that each variable is arc consistent.
        If `arcs` is None, begin with initial list of all arcs in the problem.
        Otherwise, use `arcs` as the initial list of arcs to make consistent.

        Return True if arc consistency is enforced and no domains are empty;
        return False if one or more domains end up empty.
        """
        queue = []
        if arcs == None: #populate queue with all possible arcs
            for a in self.domains:
                    for b in self.domains:
                        if a != b and self.crossword.overlaps[a,b] != None: #if two domains have an overlap and are different, then is arc
                            if (a,b) not in queue: queue.append((a,b))
        else:
            for arc in arcs: #just add to queue what was passed into method call
                queue.append(arc)
        while True:
            try:
                x, y = queue.pop()
            except IndexError: #queue is empty, so proud.
                return True
            if self.revise(x,y) == True:
                if len(self.domains[x]) == 0:
                    return False #emptied domain, no solution to puzzle
                '''for j in self.domains:
                    if j == x:
                        continue
                    if self.crossword.overlaps[x,j] != None:
                        queue.append((x,j))'''
                xneighbors = self.crossword.neighbors(x)
                yneighbors = self.crossword.neighbors(y)
                for each in xneighbors: queue.append((x, each))
                for each in yneighbors: queue.append((y, each))
                
        raise NotImplementedError

    def assignment_complete(self, assignment):
        """
        Return True if `assignment` is complete (i.e., assigns a value to each
        crossword variable); return False otherwise.
        """
        for var in self.domains:
            try:
                assignment[var]
            except KeyError:
                return False
        return True  # otherwise is complete.
        
        raise NotImplementedError

    def consistent(self, assignment):
        """
        Return True if `assignment` is consistent (i.e., words fit in crossword
        puzzle without conflicting characters); return False otherwise.
        """
        for x in assignment:
            for y in assignment:
                if x != y:
                    if assignment[x] == assignment[y]:  # word duplication check
                        return False
                    overlap = self.crossword.overlaps[x,y] 
                    if overlap:
                        if assignment[x][overlap[0]] != assignment[y][overlap[1]]:
                            return False  # intersecting letter not the same in arc
                        '''may need check for words existing in same non-overlap cell, or in black cells'''
            if len(assignment[x]) != x.length:
                return False  # word length check 
        return True
                    
        raise NotImplementedError

    def order_domain_values(self, var, assignment):
        """
        Return a list of values in the domain of `var`, in order by
        the number of values they rule out for neighboring variables.
        The first value in the list, for example, should be the one
        that rules out the fewest values among the neighbors of `var`.
        """
        all_var = [v for v in self.domains]
        neighbors = self.crossword.neighbors(var) # dict containing neighbor and overlap
        values = list(self.domains[var]) #  list of values in var's domain
        constraints = {} # will hold value:constraits pairs for sorting order 
        
        """for v in all_var: # iterates through all variables, if it has no assignment, and overlaps with var, it is neighbor
            if v not in assignment:
                overlaps = self.crossword.overlaps[var,v]
                if overlaps:
                    for overlap in overlaps:
                        neighbors[v].append = overlap"""
        for value in values:
            elim = 0  # how many values in neighbords eliminated
            for neighbor in neighbors:
                overlap = self.crossword.overlaps[var, neighbor]
                for nWord in self.domains[neighbor]:
                    if value[overlap[0]] != nWord[overlap[1]]:
                        elim += 1
            constraints[value] = elim


        values.sort(key = lambda v : constraints[v])
        return values
    
        raise NotImplementedError

    def select_unassigned_variable(self, assignment):
        """
        Return an unassigned variable not already part of `assignment`.
        Choose the variable with the minimum number of remaining values
        in its domain. If there is a tie, choose the variable with the highest
        degree. If there is a tie, any of the tied variables are acceptable
        return values.
        """
        #unassigned_var = { v : [len(self.domains[v]), self.crossword.neighbors(v)] for v in self.domains if v not in assignment}
        unassigned_var = {}
        for v in self.domains:
            if v not in assignment:
                neighbors = self.crossword.neighbors(v)
                unassigned_var[v] = [len(self.domains[v]), neighbors]
        '''neighbors = 0
        for x in unassigned_var:
            for y in self.domains:
                if self.crossword.overlap[x,y] : neighbors += 1
            unassigned_var[x].append(neighbors)
            neighbors = 0'''
        pick = sorted(unassigned_var, key = lambda var : unassigned_var[var][0], reverse= False)  # sort dict by length of domain
        pick = sorted(pick, key = lambda var : len(unassigned_var[var][1]), reverse = True)  # sort dict by degrees, high to low

        return pick[0] # return lowest length, highest degree variable
    
        raise NotImplementedError

    def backtrack(self, assignment):
        """
        Using Backtracking Search, take as input a partial assignment for the
        crossword and return a complete assignment if possible to do so.

        `assignment` is a mapping from variables (keys) to words (values).

        If no assignment is possible, return None.
        """
        
        if self.assignment_complete(assignment) == True:
            return assignment
        else:
            var = self.select_unassigned_variable(assignment)
            for value in self.domains[var]:
                if self.consistent(assignment):
                    assignment[var] = value
                    result = self.backtrack(assignment)
                    if result != None:
                        return assignment
                    del assignment[var]
        return None
        raise NotImplementedError


def main():

    # Check usage
    if len(sys.argv) not in [3, 4]:
        sys.exit("Usage: python generate.py structure words [output]")

    # Parse command-line arguments
    structure = sys.argv[1]
    words = sys.argv[2]
    output = sys.argv[3] if len(sys.argv) == 4 else None

    # Generate crossword
    crossword = Crossword(structure, words)
    creator = CrosswordCreator(crossword)
    assignment = creator.solve()

    # Print result
    if assignment is None:
        print("No solution.")
    else:
        creator.print(assignment)
        if output:
            creator.save(assignment, output)


if __name__ == "__main__":
    main()
