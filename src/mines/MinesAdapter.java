package mines;

import java.awt.event.MouseAdapter;
import java.awt.event.MouseEvent;

class MinesAdapter extends MouseAdapter {
    Board board;
    public MinesAdapter(Board b) {
        this.board=b;
    }
    @Override
    public void mousePressed(MouseEvent e) {

        int x = e.getX();
        int y = e.getY();

        int cCol = x / Board.CELL_SIZE;
        int cRow = y / Board.CELL_SIZE;

        boolean rep = false;


        if (!this.board.inGame) {
            this.board.newGame();
            this.board.repaint();
        }


        if ((x < this.board.cols * Board.CELL_SIZE) && (y < this.board.rows * Board.CELL_SIZE)) {

            if (e.getButton() == MouseEvent.BUTTON3) {

                if (this.board.field[(cRow * this.board.cols) + cCol] > Board.MINE_CELL) {
                    rep = true;

                    if (this.board.field[(cRow * this.board.cols) + cCol] <= Board.COVERED_MINE_CELL) {
                        if (this.board.minesLeft > 0) {
                            this.board.field[(cRow * this.board.cols) + cCol] += Board.MARK_FOR_CELL;
                            this.board.minesLeft--;
                            this.board.statusbar.setText(Integer.toString(this.board.minesLeft));
                        } else
                            this.board.statusbar.setText("No marks left");
                    } else {

                        this.board.field[(cRow * this.board.cols) + cCol] -= Board.MARK_FOR_CELL;
                        this.board.minesLeft++;
                        this.board.statusbar.setText(Integer.toString(this.board.minesLeft));
                    }
                }

            } else {

                if (this.board.field[(cRow * this.board.cols) + cCol] > Board.COVERED_MINE_CELL) {
                    return;
                }

                if ((this.board.field[(cRow * this.board.cols) + cCol] > Board.MINE_CELL) &&
                        (this.board.field[(cRow * this.board.cols) + cCol] < Board.MARKED_MINE_CELL)) {

                    this.board.field[(cRow * this.board.cols) + cCol] -= Board.COVER_FOR_CELL;
                    rep = true;

                    if (this.board.field[(cRow * this.board.cols) + cCol] == Board.MINE_CELL)
                        this.board.inGame = false;
                    if (this.board.field[(cRow * this.board.cols) + cCol] == Board.EMPTY_CELL)
                        this.board.find_empty_cells((cRow * this.board.cols) + cCol);
                }
            }

            if (rep)
                this.board.repaint();

        }
    }
}
