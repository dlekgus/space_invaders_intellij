package spaceinvaders.entity;

import spaceinvaders.Game;
import spaceinvaders.Sprite;
import spaceinvaders.SpriteStore;

/**
 * An entity which represents one of our space invader aliens.
 *
 * @author Kevin Glass
 */
public class BossEntity extends Entity {
    /** The speed at which the alient moves horizontally */
    /** The game in which the entity exists */
    private Game game;
    /** The animation frames */
    private Sprite[] frames = new Sprite[4];
    /** The time since the last frame change took place */
    private long lastFrameChange;
    /** The frame duration in milliseconds, i.e. how long any given frame of animation lasts */
    private long frameDuration = 250;
    /** The current frame of animation being displayed */
    private int frameNumber;
    private LifeCounter bossLifes;
    /**
     * Create a new alien entity
     *
     * @param game The game in which this entity is being created
     * @param s
     * @param x    The intial x location of this alien
     * @param y    The intial y location of this alient
     */
    public BossEntity(Game game, String s, int x, int y) {
        super("sprites/boss1.png", x, y);
        frames[0] = sprite;
        frames[1] = SpriteStore.get().getSprite("sprites/boss2.png");
        frames[2] = sprite;
        frames[3] = SpriteStore.get().getSprite("sprites/boss3.png");
        bossLifes = new LifeCounter(game, this, null, 30);
        this.game = game;
        dx = -game.getAlienHoriSpeed();
        dy = game.getAlienVertSpeed();
    }



//     * Request that this alien moved based on time elapsed
//     *
//     * @param delta The time that has elapsed since last move
//     */
    public void move(long delta) {
        // since the move tells us how much time has passed
        // by we can use it to drive the animation, however
        // its the not the prettiest solution
        lastFrameChange += delta;

        // if we need to change the frame, update the frame number
        // and flip over the sprite in use
        if (lastFrameChange > frameDuration) {
            // reset our frame change time counter
            lastFrameChange = 0;

            // update the frame
            frameNumber++;
            if (frameNumber >= frames.length) {
                frameNumber = 0;
            }

            sprite = frames[frameNumber];
        }

        // if we have reached the left hand side of the screen and
        // are moving left then request a logic update
        if ((dx < 0) && (x < 10)) {
            game.updateLogic();
        }
        // and vice vesa, if we have reached the right hand side of
        // the screen and are moving right, request a logic update
        if ((dx > 0) && (x > 750)) {
            game.updateLogic();
        }

        // proceed with normal move
        super.move(delta);
    }

    /**
     * Update the game logic related to aliens
     */
    public void doLogic() {
        // swap over horizontal movement and move down the
        // screen a bit
        dx = -dx;
        y += 10;

        // if we've reached the bottom of the screen then the player
        // dies
        if (y > 570) {
            game.notifyRetire();
        }
    }
    /**
     * Notification that this alien has collided with another entity
     *
     * @param other The other entity
     */
    public void collidedWith(Entity other) {
        // if we've hit an alien, kill it!
        if (other instanceof ShotEntity){
            if (bossLifes.getEntityLife()%7 == 0) {
                int x = this.getX() + 10; // 초기 x 좌표 설정
                int y = this.getY() + 10; // 초기 y 좌표 설정

// 첫 번째 공격
                AttackEntity attack1 = new AttackEntity(game, "sprites/bossAttack.png", x, y);
                game.addEntity(attack1);

// 두 번째 공격
                y += 50; // y 좌표를 증가시켜 다음 위치로 조정
                AttackEntity attack2 = new AttackEntity(game, "sprites/bossAttack.png", x, y);
                game.addEntity(attack2);

// 세 번째 공격
                y += 50; // y 좌표를 다시 증가시켜 다음 위치로 조정
                AttackEntity attack3 = new AttackEntity(game, "sprites/bossAttack.png", x, y);
                game.addEntity(attack3);

            }
            if (bossLifes.getEntityLife()==1) {
                game.notifyAlienKilled(this,100);
                bossLifes.LifeDecrease();
                game.removeEntity(this);
            }
            else{
                bossLifes.LifeDecrease();
            }
        }
    }
}