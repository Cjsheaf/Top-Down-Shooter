package TopDownShooter.Utility;

import java.awt.Point;
import java.lang.reflect.Array;

/*
 * - A 2D array which acts like a circular queue.
 * - Whenever items are pushed onto the array from any direction, the items on the opposite side are removed to free up space.
 * - The physical indexes of any items present in the array do not change, saving on processing time. Instead, an offset from [0][0] is used to
 * represent the "relative" [0][0] of the array.
 * - An example would be:
 *  [1][2][3]          [0]             [0][1][2]     With a   
 *  [4][5][6] -> Push  [0] -> Results  [0][4][5] -> relative  (1,0)
 *  [7][8][9]    Left  [0]      in:    [0][7][8]    zero of:  
 *  
 *  - *THIS PARTICULAR IMPLEMENTATION OF A CIRCULAR ARRAY ASSUMES THAT ALL CELLS ARE FILLED AT ALL TIMES*
 */
public class Circular2DArray <E> {
    private E[][] itemArray;
    private Point offset; //The X and Y offset from [0][0]. (Where the relative [0][0] of the array is located)
    
    @SuppressWarnings({"unchecked"})
    public Circular2DArray(Class<E> c, int sizeX, int sizeY) { //If you wanted to hold Strings, you would use: "new Circuclar2DArray(String.class, 3, 3);"
        itemArray = (E[][]) Array.newInstance(c, sizeX, sizeY);
        offset = new Point(0, 0);
    }
    
    public void setOffset(Point new_offset) { //[TEST] Manually sets the offset of the array
        offset = new_offset;
    }
    
    /** Sets the relative array location **/
    public void set(Point index, E item) {
        if ((index.x >= itemArray.length) || (index.x < 0)) {
            throw new ArrayIndexOutOfBoundsException();
        } else if ((index.y >= itemArray[0].length) || (index.y < 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        Point absoluteIndex = getAbsoluteIndex(index);
        itemArray[absoluteIndex.x][absoluteIndex.y] = item;
    }
    /** Gets the relative array location **/
    public E get(Point index) {
        if ((index.x >= itemArray.length) || (index.x < 0)) {
            throw new ArrayIndexOutOfBoundsException();
        } else if ((index.y >= itemArray[0].length) || (index.y < 0)) {
            throw new ArrayIndexOutOfBoundsException();
        }
        
        Point absoluteIndex = getAbsoluteIndex(index);
        return itemArray[absoluteIndex.x][absoluteIndex.y];
    }
    
    public Point size() {
        return new Point(itemArray.length, itemArray[0].length);
    }
    
    public void pushLeft(E[] column) {
        if (offset.x == 0) {
            offset.x = itemArray.length - 1;
        } else {
            offset.x--;
        }
        
        Point absoluteIndex;
        for (int y = 0; y < itemArray[0].length; y++) {
            absoluteIndex = getAbsoluteIndex(new Point(0, y));
            itemArray[absoluteIndex.x][absoluteIndex.y] = column[y];
        }
    }
    public void pushRight(E[] column) {
        if (offset.x == itemArray.length - 1) {
            offset.x = 0;
        } else {
            offset.x++;
        }
        
        Point absoluteIndex;
        for (int y = 0; y < itemArray[0].length; y++) {
            absoluteIndex = getAbsoluteIndex(new Point(itemArray.length - 1, y));
            itemArray[absoluteIndex.x][absoluteIndex.y] = column[y];
        }
    }
    
    public void pushTop(E[] row) {
        if (offset.y == 0) {
            offset.y = itemArray[0].length - 1;
        } else {
            offset.y--;
        }
        
        Point absoluteIndex;
        for (int x = 0; x < itemArray.length; x++) {
            absoluteIndex = getAbsoluteIndex(new Point(x, 0));
            itemArray[absoluteIndex.x][absoluteIndex.y] = row[x];
        }
    }
    public void pushBottom(E[] row) {
        if (offset.y == itemArray[0].length - 1) {
            offset.y = 0;
        } else {
            offset.y++;
        }
        
        Point absoluteIndex;
        for (int x = 0; x < itemArray.length; x++) {
            absoluteIndex = getAbsoluteIndex(new Point(x, itemArray[0].length - 1));
            itemArray[absoluteIndex.x][absoluteIndex.y] = row[x];
        }
    }
    
    
    
    /** Utility Methods **/
    private Point getAbsoluteIndex(Point relativeIndex) {
        return new Point(
            (relativeIndex.x + offset.x) % itemArray.length,
            (relativeIndex.y + offset.y) % itemArray[0].length
        );
    }
}