import pandas as pd

def min_available(a, b):
    if a is not None and b is not None:
        return min(a, b)
    if a is None:
        return b
    return a