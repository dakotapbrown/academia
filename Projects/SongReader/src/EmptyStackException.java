
public class EmptyStackException extends RuntimeException
{
	private static final long serialVersionUID = 586305334259144321L;

	/**
     * Constructs a EmptyStackException with no detail message.
     */
    public EmptyStackException( )
    {
    }
    
    /*
     * Constructs a EmptyStackException with a detail message.
     * @param msg the detail mesage pertaining to this exception.
     */
    public EmptyStackException( String msg )
    {
        super( msg );
    }
}