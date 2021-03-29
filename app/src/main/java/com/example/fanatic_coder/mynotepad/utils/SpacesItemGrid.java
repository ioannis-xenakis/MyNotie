package com.example.fanatic_coder.mynotepad.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;

/**
 * SpacesItemGrid, is a class for adding space between items(for ex. notes, in notes list),
 * when multiple columns are in grid.
 * @author John/Ioannis Xenakis
 * @version 1.0
 */
public class SpacesItemGrid  extends RecyclerView.ItemDecoration {
    /**
     * The spacing/space in pixels.
     */
    private final int space;
    /**
     * The number of columns.
     */
    private final int spanCount;

    /**
     * The Constructor needed for the SpacesItemGrid class, to be initialized and called in another class/activity.
     * @param space the space/spacing.
     * @param spanCount the number of columns.
     */
    public SpacesItemGrid(int space, int spanCount) {
        this.space = space;
        this.spanCount = spanCount;
    }

    /**
     * getItemOffsets is the main method to be called, for calculating the spaces needed for each item(or should say note).
     * @param outRect the direction to be added space.
     * @param view the item,(or note).
     * @param parent the RecyclerView,(or notes list).
     * @param state the current state of the RecyclerView(or notes list).
     */
    @Override
    public void getItemOffsets(@NonNull @NotNull Rect outRect, @NonNull @NotNull View view, @NonNull @NotNull RecyclerView parent, @NonNull @NotNull RecyclerView.State state) {
        int position = parent.getChildAdapterPosition(view); // item position.
        int column = position % spanCount; // item column.

        outRect.left = column * space / spanCount;
        outRect.right = space - (column + 1) * space / spanCount;
    }
}
