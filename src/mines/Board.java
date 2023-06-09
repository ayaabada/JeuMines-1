package mines;

import java.awt.Graphics;
import java.awt.Image;
import java.security.SecureRandom;

import javax.swing.ImageIcon;
import javax.swing.JLabel;
import javax.swing.JPanel;


public class Board extends JPanel {
	private static final long serialVersionUID = 6195235521361212179L;
	
	protected static final int NUM_IMAGES = 13;
    protected static final int CELL_SIZE = 15;

    protected static final int COVER_FOR_CELL = 10;
    protected static final int MARK_FOR_CELL = 10;
    protected static final int EMPTY_CELL = 0;
    protected static final int MINE_CELL = 9;
    protected static final int COVERED_MINE_CELL = MINE_CELL + COVER_FOR_CELL;
    protected static final int MARKED_MINE_CELL = COVERED_MINE_CELL + MARK_FOR_CELL;

    protected static final int DRAW_MINE = 9;
    protected static final int DRAW_COVER = 10;
    protected static final int DRAW_MARK = 11;
    protected static final int DRAW_WRONG_MARK = 12;

    protected int[] field;
    protected boolean inGame;
    protected int minesLeft;
    protected  transient Image[] img;
    protected int mines = 40;
    protected int rows = 16;
    protected int cols = 16;
    protected int allCells;
    protected JLabel statusbar;
    SecureRandom random = new SecureRandom();


    public Board(JLabel statusbar) {

        this.statusbar = statusbar;

        img = new Image[NUM_IMAGES];

        for (int i = 0; i < NUM_IMAGES; i++) {
			img[i] =
                    (new ImageIcon(getClass().getClassLoader().getResource((i)
            			    + ".gif"))).getImage();
        }

        setDoubleBuffered(true);

        addMouseListener(new MinesAdapter(this));
        newGame();
    }


    public void newGame() {
        initializeGame();
        deployMines();
    }

    private void initializeGame() {
        inGame = true;
        minesLeft = mines;
        allCells = rows * cols;
        field = new int[allCells];
        for (int i = 0; i < allCells; i++) {
            field[i] = COVER_FOR_CELL;
        }
        statusbar.setText(Integer.toString(minesLeft));
    }

    private void deployMines() {

        int mines_deployed = 0;
        while (mines_deployed < mines) {
            int position = (int) (allCells * random.nextDouble());
            if ((position < allCells) && (field[position] != COVERED_MINE_CELL)) {
                field[position] = COVERED_MINE_CELL;
                mines_deployed++;
                incrementAdjacentCells(position);
            }
        }
    }

    private void incrementAdjacentCells(int position) {
        int current_col = position % cols;
        if (current_col > 0) {
            int cell = position - 1 - cols;
            if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                field[cell]++;
            }
            cell = position - 1;
            if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                field[cell]++;
            }
            cell = position + cols - 1;
            if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                field[cell]++;
            }
        }
        int cell = position - cols;
        if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
            field[cell]++;
        }
        cell = position + cols;
        if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
            field[cell]++;
        }
        if (current_col < (cols - 1)) {
            cell = position - cols + 1;
            if (cell >= 0 && field[cell] != COVERED_MINE_CELL) {
                field[cell]++;
            }
            cell = position + cols + 1;
            if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                field[cell]++;
            }
            cell = position + 1;
            if (cell < allCells && field[cell] != COVERED_MINE_CELL) {
                field[cell]++;
            }
        }
    }



    public void find_empty_cells(int j) {

        int currentCol = j % cols;
        int cell;

        if (currentCol > 0) {
            cell = j - cols - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }

            cell = j - 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }

            cell = j + cols - 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
        }

        cell = j - cols;
        if (cell >= 0)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }

        cell = j + cols;
        if (cell < allCells)
            if (field[cell] > MINE_CELL) {
                field[cell] -= COVER_FOR_CELL;
                if (field[cell] == EMPTY_CELL)
                    find_empty_cells(cell);
            }

        if (currentCol < (cols - 1)) {
            cell = j - cols + 1;
            if (cell >= 0)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }

            cell = j + cols + 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }

            cell = j + 1;
            if (cell < allCells)
                if (field[cell] > MINE_CELL) {
                    field[cell] -= COVER_FOR_CELL;
                    if (field[cell] == EMPTY_CELL)
                        find_empty_cells(cell);
                }
        }

    }
    @Override

    public void paint(Graphics g) {
        int uncover = 0;

        for (int i = 0; i < rows; i++) {
            for (int j = 0; j < cols; j++) {
                int cell = field[(i * cols) + j];
                int drawCell = DRAW_COVER;

                if (inGame) {
                    if (cell > COVERED_MINE_CELL) {
                        drawCell = DRAW_MARK;
                    } else if (cell > MINE_CELL) {
                        drawCell = DRAW_COVER;
                        uncover++;
                    }
                } else {
                    if (cell == COVERED_MINE_CELL || cell == MARKED_MINE_CELL) {
                        drawCell = DRAW_MINE;
                    } else if (cell > COVERED_MINE_CELL) {
                        drawCell = DRAW_WRONG_MARK;
                    }
                }

                g.drawImage(img[drawCell], (j * CELL_SIZE), (i * CELL_SIZE), this);
            }
        }

        if (!inGame) {
            statusbar.setText("Game lost");
        } else if (uncover == 0) {
            inGame = false;
            statusbar.setText("Game won");
        }
    }





}