package j.hig.tictactoe;

import android.animation.Animator;
import android.animation.AnimatorInflater;
import android.graphics.drawable.Drawable;
import android.view.View;
import android.widget.ImageButton;

/**
 * Created by Miran on 22/11/2017.
 */

public class Tile {

    public Tile deepCopy()
    {
        Tile tile = new Tile(mGame);
        tile.setOwner(getOwner());
        if(getSubTiles() != null)
        {
            Tile newTiles[] = new Tile[9];
            Tile oldTiles[] = getSubTiles();
            for (int child = 0; child < 9; child++)
            {
                newTiles[child] = oldTiles[child].deepCopy();
            }
            tile.setSubTiles(newTiles);
        }
        return tile;
    }

    public void animate()
    {
        Animator anim = AnimatorInflater.loadAnimator(mGame.getActivity(), R.animator.tictactoe);
        if(getView() != null)
        {
            anim.setTarget(getView());
            anim.start();
        }
    }

    public enum Owner
    {
        X, O, NEITHER, BOTH
    }

    private static final int LEVEL_X = 0;
    private static final int LEVEL_O = 1;
    private static final int LEVEL_BLANK = 2;
    private static final int LEVEL_AVAILABLE = 3;
    private static final int LEVEL_TIE = 3;

    private View mView;
    private GameFragment mGame;
    private Owner mOwner = Owner.NEITHER;
    private Tile mSubTiles[];

    public Tile(GameFragment gameFragment)
    {
        this.mGame = gameFragment;
    }

    public void setSubTiles(Tile[] subTiles)
    {
        this.mSubTiles = subTiles;
    }

    public void setView(View mView)
    {
        this.mView = mView;
    }

    public void setOwner(Owner owner)
    {
        this.mOwner = owner;
    }

    public Owner getOwner()
    {
        return mOwner;
    }

    public View getView()
    {
        return mView;
    }

    public Tile[] getSubTiles()
    {
        return mSubTiles;
    }

    public Owner findWinner()
    {
        if(getOwner() !=  Owner.NEITHER)
                return getOwner();

        int totalX[] = new int[4];
        int totalO[] = new int[4];
        countCaptures(totalX, totalO);
        if(totalX[3] > 0)
            return Owner.X;
        if(totalO[3] > 0)
            return Owner.O;

        //Check if draw
        int total = 0;
        for (int row = 0; row < 3; row++)
        {
            for(int col = 0; col < 3; col++)
            {
                Owner owner = mSubTiles[3*row+col].getOwner();
                if(owner != Owner.NEITHER)
                    total++;
            }
            if(total == 9)
                return Owner.BOTH;
        }
        return Owner.NEITHER; //stub
    }

    private void countCaptures(int[] totalX, int[] totalO)
    {
        int capturedX, captureO;

        //check horizontal
        for(int row = 0; row < 3; row++)
        {
            capturedX = captureO = 0;
            for(int col = 0; col < 3; col++)
            {
                Owner owner = mSubTiles[3*row+col].getOwner();
                if(owner == Owner.X || owner == Owner.BOTH)
                    capturedX++;
                if(owner == Owner.O || owner == Owner.BOTH)
                    captureO++;
            }
            totalO[captureO]++;
            totalX[capturedX]++;
        }

        //check vertical
        for(int col = 0; col < 3; col++)
        {
            capturedX = captureO = 0;
            for(int row = 0; row < 3; row++)
            {
                Owner owner = mSubTiles[3*row+col].getOwner();
                if(owner == Owner.X || owner == Owner.BOTH)
                    capturedX++;
                if(owner == Owner.O || owner == Owner.BOTH)
                    captureO++;
            }
            totalO[captureO]++;
            totalX[capturedX]++;
        }

        //check diagonal
        capturedX = captureO = 0;
        for(int diag = 0; diag < 3; diag++)
        {
            Owner owner = mSubTiles[3*diag+diag].getOwner();
            if(owner == Owner.X || owner == Owner.BOTH)
                capturedX++;
            if(owner == Owner.O || owner == Owner.BOTH)
                captureO++;
        }
        totalO[captureO]++;
        totalX[capturedX]++;

        capturedX = captureO = 0;
        for(int diag = 0; diag < 3; diag++)
        {
            Owner owner = mSubTiles[3*diag + (2 - diag)].getOwner();
            if(owner == Owner.X || owner == Owner.BOTH)
                capturedX++;
            if(owner == Owner.O || owner == Owner.BOTH)
                captureO++;
        }
        totalO[captureO]++;
        totalX[capturedX]++;
    }

    public void updateDrawableState()
    {
        if(mView == null)
            return;
        int level = getLevel();
        if(mView.getBackground() != null)
        {
            mView.getBackground().setLevel(level);
        }
        if(mView instanceof ImageButton) //TODO: find better way than instanceof?
        {
            Drawable drawable = ((ImageButton) mView).getDrawable();
            drawable.setLevel(level);
        }
    }

    private int getLevel()
    {
        int level = LEVEL_BLANK;

        switch (mOwner)
        {
            case X:
                level = LEVEL_X;
                break;
            case O:
                level = LEVEL_O;
                break;
            case BOTH:
                level = LEVEL_TIE;
                break;
            case NEITHER:
                level = mGame.isAvailable(this) ? LEVEL_AVAILABLE : LEVEL_BLANK;
                break;
        }
        return level;
    }

    public int evaluate()
    {
        switch (getOwner())
        {
            case X:
                return 100;
            case O:
                return -100;
            case NEITHER:
                int total = 0;
                if (getSubTiles() != null)
                {
                    for (int tile = 0; tile < 9; tile++)
                        total += getSubTiles()[tile].evaluate();
                        int totalX[] = new int[4];
                        int totalO[] = new int[4];
                        countCaptures(totalX, totalO);
                        total = total * 100 + totalX[1] + 2 * totalX[2] + 8 * totalX[3] - totalO[1] - 2 *totalO[2] - 8 * totalO[3];
                }
                return total;
        }
        return 0;
    }
}
