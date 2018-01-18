package facebroke.util;

import groovy.transform.PackageScope

import java.nio.ByteBuffer
import java.security.SecureRandom
import org.slf4j.Logger
import org.slf4j.LoggerFactory


class LoremGenerator {
	@PackageScope final static Logger log = LoggerFactory.getLogger(LoremGenerator.class)
	
	@PackageScope static final def words = ["lorem", "ipsum", "dolor", "sit", "amet", "consectetur",
				 "adipiscing", "elit", "curabitur", "vel", "hendrerit", "libero",
				 "eleifend", "blandit", "nunc", "ornare", "odio", "ut",
				 "orci", "gravida", "imperdiet", "nullam", "purus", "lacinia",
				 "a", "pretium", "quis", "congue", "praesent", "sagittis",
				 "laoreet", "auctor", "mauris", "non", "velit", "eros",
				 "dictum", "proin", "accumsan", "sapien", "nec", "massa",
				 "volutpat", "venenatis", "sed", "eu", "molestie", "lacus",
				 "quisque", "porttitor", "ligula", "dui", "mollis", "tempus",
				 "at", "magna", "vestibulum", "turpis", "ac", "diam",
				 "tincidunt", "id", "condimentum", "enim", "sodales", "in",
				 "hac", "habitasse", "platea", "dictumst", "aenean", "neque",
				 "fusce", "augue", "leo", "eget", "semper", "mattis",
				 "tortor", "scelerisque", "nulla", "interdum", "tellus", "malesuada",
				 "rhoncus", "porta", "sem", "aliquet", "et", "nam",
				 "suspendisse", "potenti", "vivamus", "luctus", "fringilla", "erat",
				 "donec", "justo", "vehicula", "ultricies", "varius", "ante",
				 "primis", "faucibus", "ultrices", "posuere", "cubilia", "curae",
				 "etiam", "cursus", "aliquam", "quam", "dapibus", "nisl",
				 "feugiat", "egestas", "class", "aptent", "taciti", "sociosqu",
				 "ad", "litora", "torquent", "per", "conubia", "nostra",
				 "inceptos", "himenaeos", "phasellus", "nibh", "pulvinar", "vitae",
				 "urna", "iaculis", "lobortis", "nisi", "viverra", "arcu",
				 "morbi", "pellentesque", "metus", "commodo", "ut", "facilisis",
				 "felis", "tristique", "ullamcorper", "placerat", "aenean", "convallis",
				 "sollicitudin", "integer", "rutrum", "duis", "est", "etiam",
				 "bibendum", "donec", "pharetra", "vulputate", "maecenas", "mi",
				 "fermentum", "consequat", "suscipit", "aliquam", "habitant", "senectus",
				 "netus", "fames", "quisque", "euismod", "curabitur", "lectus",
				 "elementum", "tempor", "risus", "cras"]
	
	
	def avgSentenceLength = 8
	def minSentenceLength = 3
	def upperRandom = (avgSentenceLength - minSentenceLength) * 2
	def r
	
	
	def LoremGenerator(seed) {
		if (!seed) {
			r = new SecureRandom()
		}else {
			ByteBuffer buffer = ByteBuffer.allocate(Long.BYTES)
			buffer.putLong(seed)
			r = new SecureRandom(buffer.array())
		}
	}
	
	
	def getWords(len) {
		if (len < 1) {
			""
		}else {
			def buffer = ""
			
			for (i in 0..len-1) {
				buffer += words[r.nextInt(words.size())]
				if (i+1 < len) {
					buffer += " "
				}
			}
			
			buffer.substring(0,1).toUpperCase() + buffer.substring(1)
		}
	}
	
	
	String getSentences(numSentences) {
		if (numSentences < 1) {
			""
		}else {
			def buffer = ""
			
			for (i in 0..numSentences) {
				buffer += getWords(minSentenceLength + r.nextInt(upperRandom)) + "."
				
				if (i+1 < numSentences) {
					buffer += " "
				}
			}
			
			buffer
		}
	}
	
	
	def getAvgSentenceLength() {
		avgSentenceLength
	}
	
	
	def setAvgSentenceLength(avgSentenceLength) {
		this.avgSentenceLength = avgSentenceLength
		this.upperRandom = (avgSentenceLength - minSentenceLength) * 2
	}
	
	
	def getShortestSentenceLength() {
		minSentenceLength
	}
	
	
	def setShortestSentenceLength(shortestSentenceLength) {
		minSentenceLength = shortestSentenceLength
		upperRandom = (avgSentenceLength - minSentenceLength) * 2
	}
}