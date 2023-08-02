package org.apache.maven.archetypes;
public class DuplicateObjectException extends RuntimeException {
    public DuplicateObjectException() {
        super("Are you you sure you haven't enter this somewhere?");
    }
}
