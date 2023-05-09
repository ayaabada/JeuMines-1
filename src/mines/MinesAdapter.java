package mines;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MinesAdapter extends MouseAdapter {
    Board board;
    public MinesAdapter(Board b) {
        this.board=b;
    }
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        int cCol = x / CELL_SIZE;
        int cRow = y / CELL_SIZE;

        boolean rep = false;


        if (!inGame) {
            newGame();
            repaint();
        }


        if ((x < cols * CELL_SIZE) && (y < rows * CELL_SIZE)) {

            if (e.getButton() == MouseEvent.BUTTON3) {

                if (field[(cRow * cols) + cCol] > MINE_CELL) {
                    rep = true;

                    if (field[(cRow * cols) + cCol] <= COVERED_MINE_CELL) {
                        if (mines_left > 0) {
                            field[(cRow * cols) + cCol] += MARK_FOR_CELL;
                            mines_left--;
                            statusbar.setText(Integer.toString(mines_left));
                        } else
                            statusbar.setText("No marks left");
                    } else {

                        field[(cRow * cols) + cCol] -= MARK_FOR_CELL;
                        mines_left++;
                        statusbar.setText(Integer.toString(mines_left));
                    }
                }

            } else {

                if (field[(cRow * cols) + cCol] > COVERED_MINE_CELL) {
                    return;
                }

                if ((field[(cRow * cols) + cCol] > MINE_CELL) &&
                        (field[(cRow * cols) + cCol] < MARKED_MINE_CELL)) {

                    field[(cRow * cols) + cCol] -= COVER_FOR_CELL;
                    rep = true;

                    if (field[(cRow * cols) + cCol] == MINE_CELL)
                        inGame = false;
                    if (field[(cRow * cols) + cCol] == EMPTY_CELL)
                        find_empty_cells((cRow * cols) + cCol);
                }
            }

            if (rep)
                repaint();

        }
    }
}
