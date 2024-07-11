import nltk
import sys

TERMINALS = """
Adj -> "country" | "dreadful" | "enigmatical" | "little" | "moist" | "red"
Adv -> "down" | "here" | "never"
Conj -> "and" | "until"
Det -> "a" | "an" | "his" | "my" | "the"
N -> "armchair" | "companion" | "day" | "door" | "hand" | "he" | "himself"
N -> "holmes" | "home" | "i" | "mess" | "paint" | "palm" | "pipe" | "she"
N -> "smile" | "thursday" | "walk" | "we" | "word"
P -> "at" | "before" | "in" | "of" | "on" | "to"
V -> "arrived" | "came" | "chuckled" | "had" | "lit" | "said" | "sat"
V -> "smiled" | "tell" | "were"
"""

NONTERMINALS = """
S -> NP | NP PP
S -> NP VP | NP VP NP | NP VP NP PP 
S -> NP VP AP | NP VP PP |
S -> S Conj S | S Conj VP | S Conj VP PP
S -> NP NP PP PP
S -> VP NP PP 
AP -> Adj | Adj NP | Adj Conj Adj | Det Adj NP | Adj Adj Adj 
NP -> Det N | Det AP N | N V | NP V | NP Conj NP
PP -> P | P N | P Det N | P NP | Det N P N | P Det N Adv 
VP -> V | N V | V N | AP V | V AP | V NP | V Det NP | V Det AP
"""

grammar = nltk.CFG.fromstring(NONTERMINALS + TERMINALS)
parser = nltk.ChartParser(grammar)


def main():

    # If filename specified, read sentence from file
    if len(sys.argv) == 2:
        with open(sys.argv[1]) as f:
            s = f.read()

    # Otherwise, get sentence as input
    else:
        s = input("Sentence: ")

    # Convert input into list of words
    s = preprocess(s)

    # Attempt to parse sentence
    try:
        trees = list(parser.parse(s))
    except ValueError as e:
        print(e)
        return
    if not trees:
        print("Could not parse sentence.")
        return

    # Print each tree with noun phrase chunks
    for tree in trees:
        tree.pretty_print()

        print("Noun Phrase Chunks")
        for np in np_chunk(tree):
            print(" ".join(np.flatten()))


def preprocess(sentence):
    """
    Convert `sentence` to a list of its words.
    Pre-process sentence by converting all characters to lowercase
    and removing any word that does not contain at least one alphabetic
    character.

    Here lay this, the 1 and only s@ample s3ntence.
    """
    tokenized = nltk.tokenize.WordPunctTokenizer().tokenize(sentence)
    result: list[str] = [word.lower() for word in tokenized if word.isalpha() == True]

    return result
    raise NotImplementedError


def np_chunk(tree):
    """
    Return a list of all noun phrase chunks in the sentence tree.
    A noun phrase chunk is defined as any subtree of the sentence
    whose label is "NP" that does not itself contain any other
    noun phrases as subtrees.
    """

    def scrape(branch) -> list:
        try:
            for s in branch:
                if s.label() == "NP":
                    sub = list(s.subtrees())[1:]
                    test = [l.label() for l in sub]
                    if "NP" not in test:
                        chunks.append(s)
                scrape(s)
        except AttributeError:
             pass
            
    chunks = []
    scrape(tree)

    return chunks
    raise NotImplementedError


if __name__ == "__main__":
    main()
