import os
import random
import re
import sys
from copy import deepcopy

DAMPING = 0.85
SAMPLES = 10000


def main():
    if len(sys.argv) != 2:
        sys.exit("Usage: python pagerank.py corpus")
    corpus = crawl(sys.argv[1])
    ranks = sample_pagerank(corpus, DAMPING, SAMPLES)
    print(f"PageRank Results from Sampling (n = {SAMPLES})")
    for page in sorted(ranks):
        print(f"  {page}: {ranks[page]:.4f}")
    ranks = iterate_pagerank(corpus, DAMPING)
    print(f"PageRank Results from Iteration")
    for page in sorted(ranks):
        print(f"  {page}: {ranks[page]:.4f}")


def crawl(directory):
    """
    Parse a directory of HTML pages and check for links to other pages.
    Return a dictionary where each key is a page, and values are
    a list of all other pages in the corpus that are linked to by the page.
    """
    pages = dict()

    # Extract all links from HTML files
    for filename in os.listdir(directory):
        if not filename.endswith(".html"):
            continue
        with open(os.path.join(directory, filename)) as f:
            contents = f.read()
            links = re.findall(r"<a\s+(?:[^>]*?)href=\"([^\"]*)\"", contents)
            pages[filename] = set(links) - {filename}

    # Only include links to other pages in the corpus
    for filename in pages:
        pages[filename] = set(
            link for link in pages[filename]
            if link in pages
        )

    return pages


def transition_model(corpus, page, damping_factor):
    """
    Return a probability distribution over which page to visit next,
    given a current page.

    With probability `damping_factor`, choose a link at random
    linked to by `page`. With probability `1 - damping_factor`, choose
    a link at random chosen from all pages in the corpus.
    """
    global c
    model = {}   
    links_in_page = set()
    pages_in_corpus = set()

    for html in corpus.keys():  # populate list of all pages in corpus
        pages_in_corpus.add(html)
    for link in corpus[page]:  # populate list of all links on the given page
        links_in_page.add(link)

    c = len(pages_in_corpus)
    pRand = (1 - damping_factor) / c   # chance of any page being selected when there is no link on the page
    if len(corpus[page]) != 0:
        q = len(corpus[page])
        pLink = (damping_factor / q)  # chance of link getting clicked on a given page
        if len(links_in_page) != 0:     
            for p in pages_in_corpus:  # create transition model
                if p in links_in_page:
                    model[p] = pLink + pRand  # if a page has a link on the page we are looking at, can be chosen via link or at random
                else:
                    model[p] = pRand  # if a page isn't linked on the given page, it can only randomly be selected.
    else:
        for p in pages_in_corpus:
            model[p] = 1 / c

 
    if round(sum(model.values()), 5) == 1: 
        return model
    else:
        raise ValueError("Probablities Did Not Total 1.")
    raise NotImplementedError


def sample_pagerank(corpus, damping_factor, n):
    """
    Return PageRank values for each page by sampling `n` pages
    according to transition model, starting with a page at random.

    Return a dictionary where keys are page names, and values are
    their estimated PageRank value (a value between 0 and 1). All
    PageRank values should sum to 1.
    """
    pagerank = {}
    results = {}

    for int in range(n):
        if int == 0:
            first = random.choice(list(corpus))
            previous = transition_model(corpus, first , damping_factor)
            weights = {}        
            for w in previous:
                weights[w] = previous[w]
            results[first] = 1
        else:        
            next = list(previous)       
            previous = random.choices(next, weights.values())[0]
            if previous in results:
                results[previous] += 1
            else:
                results[previous] = 1

            previous = transition_model(corpus, previous, damping_factor)
            weights = {}        
            for w in list(previous):
                weights[w] = previous[w]
            
    for html in list(corpus):
        if html in results:
            pagerank[html] = results[html] / n
        else: 
            pagerank[html] = 0
    print(round(sum(pagerank.values()), 5))
    if round(sum(pagerank.values()), 5) == 1:
        return pagerank
    else:
        raise ValueError(f"Probabilities doesn't sum to 1.") 

    raise NotImplementedError


def iterate_pagerank(corpus, damping_factor):
    """
    Return PageRank values for each page by iteratively updating
    PageRank values until convergence.

    Return a dictionary where keys are page names, and values are
    their estimated PageRank value (a value between 0 and 1). All
    PageRank values should sum to 1.
    """
    pagerank = {}
    numlinks = {}
    d = damping_factor
    N = len(corpus.keys())
    w = 1 / N
    i = set()
    iSigma = 0
    changed = 0 
    for html in corpus:
        pagerank[html] = w
        if len(corpus[html]) == 0:
            tempSet = set()
            for t in list(corpus):
                tempSet.add(t)
                corpus[html] = tempSet
            numlinks[html] = N
        else:
            numlinks[html] = len(corpus[html])
    previous = deepcopy(pagerank)
    while True:
        for page in pagerank:
            for html in corpus:
                if page in corpus[html]:
                    i.add(html)

            if len(i) == 0:
                print("empty set")
                for html in corpus:
                    i.add(html)                
            for s in i:
                iSigma += previous[s] / numlinks[s]
            pagerank[page] = ((1-d) / N) + (d * iSigma)
            iSigma = 0
            i = set() 

            
        for p in pagerank:
            if abs(pagerank[p] - previous[p]) > .001:
                changed += 1 
        if changed == 0:
            break
        changed = 0 
        previous = deepcopy(pagerank)
        
    print("Resultsssss")
    print("||||||",round(sum(pagerank.values()), 5), "|||||||")
    if round(sum(pagerank.values()), 5) == 1:
        return pagerank

    raise NotImplementedError


if __name__ == "__main__":
    main()
