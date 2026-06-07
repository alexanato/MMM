package at.randorf.item.item_data_type;

public interface ObjectResolver<T> {

    String resolve(
            T object,
            String field);

}