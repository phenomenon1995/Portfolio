import csv
import itertools
import sys
from random import *

PROBS = {

    # Unconditional probabilities for having gene
    "gene": {
        2: 0.01,
        1: 0.03,
        0: 0.96
    },

    "trait": {

        # Probability of trait given two copies of gene
        2: {
            True: 0.65,
            False: 0.35
        },

        # Probability of trait given one copy of gene
        1: {
            True: 0.56,
            False: 0.44
        },

        # Probability of trait given no gene
        0: {
            True: 0.01,
            False: 0.99
        }
    },

    # Mutation probability
    "mutation": 0.01
}

def main():

    # Check for proper usage
    if len(sys.argv) != 2:
        sys.exit("Usage: python heredity.py data.csv")
    people = load_data(sys.argv[1])

    # Keep track of gene and trait probabilities for each person
    probabilities = {
        person: {
            "gene": {
                2: 0,
                1: 0,
                0: 0
            },
            "trait": {
                True: 0,
                False: 0
            }
        }
        for person in people
    }

    # Loop over all sets of people who might have the trait
    names = set(people)
    for have_trait in powerset(names):

        # Check if current set of people violates known information
        fails_evidence = any(
            (people[person]["trait"] is not None and
             people[person]["trait"] != (person in have_trait))
            for person in names
        )
        if fails_evidence:
            continue

        # Loop over all sets of people who might have the gene
        for one_gene in powerset(names):
            for two_genes in powerset(names - one_gene):

                # Update probabilities with new joint probability
                p = joint_probability(people, one_gene, two_genes, have_trait)
                update(probabilities, one_gene, two_genes, have_trait, p)

    # Ensure probabilities sum to 1

    normalize(probabilities)

    # Print results
    for person in people:
        print(f"{person}:")
        for field in probabilities[person]:
            print(f"  {field.capitalize()}:")
            for value in probabilities[person][field]:
                p = probabilities[person][field][value]
                print(f"    {value}: {p:.4f}")

def load_data(filename):
    """
    Load gene and trait data from a file into a dictionary.
    File assumed to be a CSV containing fields name, mother, father, trait.
    mother, father must both be blank, or both be valid names in the CSV.
    trait should be 0 or 1 if trait is known, blank otherwise.
    """
    data = dict()
    with open(filename) as f:
        reader = csv.DictReader(f)
        for row in reader:
            name = row["name"]
            data[name] = {
                "name": name,
                "mother": row["mother"] or None,
                "father": row["father"] or None,
                "trait": (True if row["trait"] == "1" else
                          False if row["trait"] == "0" else None)
            }
    return data

def powerset(s):
    """
    Return a list of all possible subsets of set s.
    """
    s = list(s)
    return [
        set(s) for s in itertools.chain.from_iterable(
            itertools.combinations(s, r) for r in range(len(s) + 1)
        )
    ]

def joint_probability(people, one_gene, two_genes, have_trait):
    """
    Compute and return a joint probability.

    The probability returned should be the probability that
        * P1 everyone in set `one_gene` has one copy of the gene, and
        * P2 everyone in set `two_genes` has two copies of the gene, and
        * P3 everyone not in `one_gene` or `two_gene` does not have the gene, and
        * P4 everyone in set `have_trait` has the trait, and
        * p5 everyone not in set` have_trait` does not have the trait.
    """
    root = []
    children = []
    lineage = {}
    order = []
   
    def parent():
        if person in two_genes:  # P(GENES)[2]
            people[person]["pGenes"] = PROBS["gene"][2]
            people[person]["nGenes"] = 2
        elif person in one_gene: # P(GENES)[1]
            people[person]["pGenes"] = PROBS["gene"][1]
            people[person]["nGenes"] = 1
        else:  #  P(GENES)[0]
            people[person]["pGenes"] = PROBS["gene"][0]
            people[person]["nGenes"] = 0

        if person in have_trait:
            people[person]["pTrait"] = PROBS["trait"][people[person]["nGenes"]][True] #TRUE OR FALSE
        else:
         # odds of person not have trait
            people[person]["pTrait"] = PROBS["trait"][people[person]["nGenes"]][False]  # Random chance of having trait.

        #jpDict[person["name"]] = person
    
    def child():
        if person in two_genes:  #P(MOM|DAD)
            pMom = heredity("mother", "yes")
            pDad = heredity("father", "yes")

            people[person]["pGenes"] = pMom * pDad
            people[person]["nGenes"] = 2
        elif person in one_gene: 
            pNotMom = heredity("mother", "no")
            pNotDad = heredity("father", "no")
            pMom = heredity("mother", "yes")
            pDad = heredity("father", "yes")
            people[person]["pGenes"] = (pMom * pNotDad) + (pNotMom * pDad)
            people[person]["nGenes"] = 1
        else:  # P(-MOM|-DAD)
            pNotMom = heredity("mother", "no")
            pNotDad = heredity("father", "no")

            people[person]["pGenes"] = pNotMom * pNotDad
            people[person]["nGenes"] = 0

        if person in have_trait:
            people[person]["pTrait"] = PROBS["trait"][people[person]["nGenes"]][True] #TRUE OR FALSE
        else:
         # odds of person not have trait
            people[person]["pTrait"] = PROBS["trait"][people[person]["nGenes"]][False]  # Random chance of having trait.       
    
    def heredity(parent, give):
        zaddy = people[person][parent]
        #print(f"Heredity: {person}'s Parent is {zaddy}")
        if people[zaddy]["nGenes"] == 0 : 
            if give == "yes":
                probability = PROBS["mutation"]
            if give == "no":
                probability = 1 - PROBS["mutation"]
        elif people[zaddy]["nGenes"] == 1 :
            if give == "yes":
                probability = .5
            if give == "no":
                probability = .5 
        elif people[zaddy]["nGenes"] == 2 :
            if give == "yes":
                probability = 1 - PROBS["mutation"]
            if give == "no":
                probability = PROBS["mutation"]
        
        return probability

    for person in people:  # create family tree
        if not people[person]["mother"] and not people[person]["father"]:
            order.append(person)
            root.append(person)
        offspring =  []
        for os in people:
            if person == people[os]["mother"] or person == people[os]["father"]:
                offspring.append(os)
                children.append(os)
        if offspring: lineage[person] = offspring
        
    for person in root:
        parent()
    for person in children:
        child()
       

    """ 
    for layer1 in lineage:    
        for person in lineage[layer1]:
            if person not in order:
                order.append(person)
                child()  
    """

  
    p1 = 0
    for each in people:
         if p1 == 0:
            p1 = people[each]["pGenes"] * people[each]["pTrait"]
         else:
             p1 *= people[each]["pGenes"] * people[each]["pTrait"]
    

    return p1
             
    raise NotImplementedError

def update(probabilities, one_gene, two_genes, have_trait, p):
    """
    Add to `probabilities` a new joint probability `p`.
    Each person should have their "gene" and "trait" distributions updated.
    Which value for each distribution is updated depends on whether
    the person is in `have_gene` and `have_trait`, respectively.
    """

    for person in probabilities:
        if person in one_gene:
            probabilities[person]["gene"][1] += p
            #print (f"{person} in  One Gene: {one_gene}")
        elif person in two_genes:
            probabilities[person]["gene"][2] += p
            #print (f"{person} in  Two Genes: {two_genes}")
        else:
            probabilities[person]["gene"][0] += p
            #print (f"{person} not in either : {one_gene, two_genes}")
        
        if person in have_trait:
            probabilities[person]["trait"][True] += p
            #print(f"{person} in have_trait: {have_trait}")
        else:
            probabilities[person]["trait"][False] += p
            #print(f"{person} not in have_trait: {have_trait}")

    return None
    raise NotImplementedError

def normalize(probabilities):
    """
    Update `probabilities` such that each probability distribution
    is normalized (i.e., sums to 1, with relative proportions the same).
    """
    for person in probabilities:
        for field in probabilities[person]:
            initial = []
            normalized = []
            for value in probabilities[person][field]:
                prob = probabilities[person][field][value]
                if prob == None or prob == "":
                    prob = 0
                initial.append(prob)
            initial.reverse()
            for i in initial:
                norm =  (i / sum(initial))
                normalized.append(norm)
                
            for value in probabilities[person][field]:
                    probabilities[person][field][value] = normalized[value]

  
if __name__ == "__main__":
    main()
