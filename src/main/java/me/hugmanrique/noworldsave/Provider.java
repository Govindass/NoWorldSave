package me.hugmanrique.noworldsave;

/**
 * @author Hugo Manrique
 * @since 03/12/2017
 */
public interface Provider {
    String getClassName() throws IllegalStateException;

    String getSaveMethodBody();

    String getNOPMethodBody();
}
