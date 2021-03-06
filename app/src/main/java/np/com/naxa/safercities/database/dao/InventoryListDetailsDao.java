package np.com.naxa.safercities.database.dao;

import androidx.lifecycle.LiveData;
import androidx.room.Dao;
import androidx.room.Insert;
import androidx.room.OnConflictStrategy;
import androidx.room.Query;

import java.util.List;

import io.reactivex.Flowable;
import np.com.naxa.safercities.database.entity.CommonPlacesAttrb;
import np.com.naxa.safercities.database.entity.Contact;
import np.com.naxa.safercities.inventory.model.InventoryListDetails;

@Dao
public interface InventoryListDetailsDao {

    @Query("SELECT * from InventoryListDetails ORDER BY iId ASC")
    Flowable<List<InventoryListDetails>> getAllInventoryListDetailsList();

    @Query("SELECT * from InventoryListDetails WHERE categoryName LIKE :category AND subcatName LIKE :subCategory")
    Flowable<List<InventoryListDetails>> getCatSubCatWiseInventoryList(String category, String subCategory);

    @Query("SELECT * from InventoryListDetails WHERE categoryName LIKE :category")
    Flowable<List<InventoryListDetails>> getCategoryWiseInventoryList(String category);

    @Query("SELECT * from InventoryListDetails WHERE subcatName LIKE :subCategory")
    Flowable<List<InventoryListDetails>> getSubCategoryWiseInventoryList(String subCategory);

    @Query("SELECT DISTINCT categoryName from InventoryListDetails")
    Flowable<List<String>> getDistinctCategoryList();

    @Query("SELECT DISTINCT subcatName from InventoryListDetails")
    Flowable<List<String>> getDistinctSubCategoryist();

    @Query("SELECT DISTINCT subcatName from InventoryListDetails WHERE categoryName LIKE :category")
    Flowable<List<String>> getDistinctSubCategoryistFromCategory(String category);

    // We do not need a conflict strategy, because the word is our primary key, and you cannot
    // add two items with the same primary key to the database. If the table has more than one
    // column, you can use @Insert(onConflict = OnConflictStrategy.REPLACE) to update a row.
    @Insert(onConflict = OnConflictStrategy.REPLACE)
    long insert(InventoryListDetails inventoryListDetails);

    @Query("DELETE FROM InventoryListDetails")
    void deleteAll();

    @Query("DELETE FROM InventoryListDetails WHERE id LIKE :unique_id")
    void deleteSpecific(String unique_id);
}
