package com.code_that_up.john_xenakis.my_notie.utils;

import android.graphics.Rect;
import android.view.View;

import androidx.annotation.NonNull;
import androidx.recyclerview.widget.RecyclerView;

import org.jetbrains.annotations.NotNull;
/*
    My Notie is a note taking app, write notes and save them to see them and remember later.
    Copyright (C) 2021  Ioannis Xenakis

    This program is free software: you can redistribute it and/or modify
    it under the terms of the GNU General Public License as published by
    the Free Software Foundation, either version 3 of the License, or
    (at your option) any later version.

    This program is distributed in the hope that it will be useful,
    but WITHOUT ANY WARRANTY; without even the implied warranty of
    MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE.  See the
    GNU General Public License for more details.

    You should have received a copy of the GNU General Public License
    along with this program.  If not, see <https://www.gnu.org/licenses/>.

    Anything you want to contact me for, contact me with an e-mail, at: Xenakis.i.Contact@gmail.com
    I'll be happy to help you, or discuss anything with you! */

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
