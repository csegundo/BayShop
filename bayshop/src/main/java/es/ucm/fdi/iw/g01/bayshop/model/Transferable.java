package es.ucm.fdi.iw.g01.bayshop.model;

/**
 * Used to json-ize objects
 */
public interface Transferable<T> {
    T toTransfer();
}
