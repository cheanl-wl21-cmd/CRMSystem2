
package interfaces;

import java.util.List;

public interface Searchable<T> {
    List<T> searchByKeyword(String keyword);
    T findById(String id);
}

