\name{term.freq}
\alias{term.freq}
\title{
  Get a table of term frequencies
}
\description{
  Get a table of term frequencies.
}
\usage{
term.freq( d, 
		topN = 0, 
		percent = 0,
		sorted = c("none", "alpha", "freq"),
		decreasing = FALSE,
		useDocFreq = FALSE,		
		minFreq = 1 )
}
\arguments{
\item{d}{The corpus from which term frequencies are calculated.}
\item{topN}{If specified, only the 'topN' most frequent terms are returned. If more terms are requested than available, all terms are returned.  If both 'topN' and 'percent' are zero, then all terms are returned.}
\item{percent}{If specified, only the top 'percent' \% most frequent terms are returned. If more terms are requested than available, all terms are returned.  If both 'topN' and 'percent' are zero, then all terms are returned.}
\item{sorted}{A string specifying how to sort the terms.  'none' for no sorting, 'alpha' for alphanumeric sorting, and 'freq' for sorting by frequency.}
\item{decreasing}{If TRUE, terms are sorted in decreasing order, if FALSE, sorted ascending order.}
\item{useDocFreq}{If TRUE, the returned frequencies are for the total number of documents in which the term occurs.  If false, they are the total number of occurrences.}
\item{minFreq}{Terms with *TOTAL* frequencies below this threshold will not be included in the output.}
}

\value{
A name vector of the term frequencies.
 }

\examples{
if(require(tm)){
data(crude)
term.freq(d=crude, percent=0, topN=10, minFreq=0,useDocFreq=FALSE,sorted="alpha", decreasing=TRUE)
}
} 
