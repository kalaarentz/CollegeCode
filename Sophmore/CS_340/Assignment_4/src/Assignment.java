import java.util.HashMap;

public interface Assignment {

    /**
     * Compress the file
     * 
     * @param inputFilename Input file to compress
     * @param outputFilename Output file to write the compressed data into
     */
    public void compressFile( String inputFilename, String outputFilename);
    
    /**
     * Decompress the file (must have been compressed with this program)
     * 
     * @param inputFilename Input file to decompress
     * @param outputFilename Output file to write the decompressed data into
     */
    public void decompressFile( String inputFilename, String outputFilename);
    
    /**
     * Access the occurrences of all substrings greater than or equal to 3 letters.
     * 
     * @param inputFilename File to read from
     * @return Map of the "substring" (String) to "frequency" (Integer) in the file
     */
    public HashMap<String, Integer> getOccurances(String inputFilename);
    
    /**
     * Access the encodings for the top 52 most occurring substrings in the file.
     * 
     * @param inputFilename File to read from
     * @return Map of the "substring" (String) to "encoding" (String, prefixed with '+')
     */
    public HashMap<String, String> getEncodings( String inputFilename );
    
}
