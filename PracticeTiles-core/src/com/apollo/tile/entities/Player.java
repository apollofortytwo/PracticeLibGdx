package com.apollo.tile.entities;

import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Input.Keys;
import com.badlogic.gdx.graphics.Texture;
import com.badlogic.gdx.graphics.g2d.Animation;
import com.badlogic.gdx.graphics.g2d.Batch;
import com.badlogic.gdx.graphics.g2d.TextureRegion;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer.Cell;
import com.badlogic.gdx.math.Vector2;

public class Player {

	private Vector2 velocity = new Vector2();
	private Vector2 position = new Vector2();
	private Vector2 dimension = new Vector2();
	private float speed = 60 * 2, gravity = 60 * 1.8f;

	Texture spriteSheet;
	TextureRegion[] animationUp, animationDown, animationLeft, animationRight, currentDefaultAnimation;
	TextureRegion[][] tmpFrames;
	Animation animation;

	private float elapsedTime;
	private TiledMapTileLayer collisionLayer;

	public Player(TiledMapTileLayer collisionLayer) {
		animationInit();
		this.setCollisionLayer(collisionLayer);
		position.x = (5 * this.collisionLayer.getHeight());
		position.y = (5 * this.collisionLayer.getHeight());
		dimension.y = currentDefaultAnimation[0].getRegionHeight();
		dimension.x = currentDefaultAnimation[0].getRegionWidth();
	}

	public void animationInit() {
		spriteSheet = new Texture(Gdx.files.internal("spriteSheet.png"));

		tmpFrames = TextureRegion.split(spriteSheet, 31, 32);

		currentDefaultAnimation = new TextureRegion[1];
		currentDefaultAnimation[0] = tmpFrames[3][1];

		animationUp = new TextureRegion[2];
		animationUp[0] = tmpFrames[3][0];
		animationUp[1] = tmpFrames[3][2];

		animationDown = new TextureRegion[2];
		animationDown[0] = tmpFrames[0][0];
		animationDown[1] = tmpFrames[0][2];

		animationLeft = new TextureRegion[2];
		animationLeft[0] = tmpFrames[1][0];
		animationLeft[1] = tmpFrames[1][2];

		animationRight = new TextureRegion[2];
		animationRight[0] = tmpFrames[2][0];
		animationRight[1] = tmpFrames[2][2];

		animation = new Animation(1 / 4f, currentDefaultAnimation);

	}

	public void render(Batch batch) {
		setElapsedTime(getElapsedTime() + Gdx.graphics.getDeltaTime());

		inputMoveHandler();

		batch.draw(animation.getKeyFrame(elapsedTime, true), position.x, position.y);

	}

	public void inputMoveHandler() {
		// Vector2 direction = new Vector2(0, 0);
		boolean collisionX = false, collisionY = false;

		if (Gdx.input.isKeyPressed(Keys.D)) { // Right
			velocity.set(+3, 0);
			animation = new Animation(1 / 4f, animationRight);
			currentDefaultAnimation[0] = tmpFrames[2][1];
		} else if (Gdx.input.isKeyPressed(Keys.A)) { // Left
			velocity.set(-3, 0);
			animation = new Animation(1 / 4f, animationLeft);
			currentDefaultAnimation[0] = tmpFrames[1][1];
		} else if (Gdx.input.isKeyPressed(Keys.W)) { // up
			velocity.set(0, +3);
			animation = new Animation(1 / 4f, animationUp);
			currentDefaultAnimation[0] = tmpFrames[3][1];
		} else if (Gdx.input.isKeyPressed(Keys.S)) { // down
			velocity.set(0, -3);
			animation = new Animation(1 / 4f, animationDown);
			currentDefaultAnimation[0] = tmpFrames[0][1];
		} else {
			velocity.set(0, 0);
			animation = new Animation(1 / 4f, currentDefaultAnimation);
		}

		if (velocity.x < 0) // going left
			collisionX = collidesLeft();
		else if (velocity.x > 0) // going right
			collisionX = collidesRight();

		if (velocity.y < 0) // going down
			collisionY = collidesBottom();
		else if (velocity.y > 0) // going up
			collisionY = collidesTop();
		
		if (collisionX) {
			velocity.x = 0;
		}
		if (collisionY) {
			velocity.y = 0;
		}
		position.x += velocity.x;
		position.y += velocity.y;

	}

	private boolean isCellBlocked(float x, float y) {
		Cell cell = collisionLayer.getCell((int) (x / collisionLayer.getTileWidth()),
				(int) (y / collisionLayer.getTileHeight()));
		return cell != null && cell.getTile() != null && cell.getTile().getProperties().containsKey("blocked");
	}

	public boolean collidesRight() {
		for (float step = 0; step < dimension.y; step += collisionLayer.getTileHeight() / 2)
			if (isCellBlocked(position.x + dimension.x, position.y + step))
				return true;
		return false;
	}

	public boolean collidesLeft() {
		for (float step = 0; step < dimension.y; step += collisionLayer.getTileHeight() / 2)
			if (isCellBlocked(position.x, position.y + step))
				return true;
		return false;
	}

	public boolean collidesTop() {
		for (float step = 0; step < dimension.x; step += collisionLayer.getTileWidth() / 2)
			if (isCellBlocked(position.x + step, position.y + dimension.y))
				return true;
		return false;

	}

	public boolean collidesBottom() {
		for (float step = 0; step < dimension.x; step += collisionLayer.getTileWidth() / 2)
			if (isCellBlocked(position.x + step, position.y))
				return true;
		return false;
	}

	public Vector2 getVelocity() {
		return velocity;
	}

	public void setVelocity(Vector2 velocity) {
		this.velocity = velocity;
	}

	public float getSpeed() {
		return speed;
	}

	public void setSpeed(float speed) {
		this.speed = speed;
	}

	public float getGravity() {
		return gravity;
	}

	public void setGravity(float gravity) {
		this.gravity = gravity;
	}

	public float getElapsedTime() {
		return elapsedTime;
	}

	public void setElapsedTime(float elapsedTime) {
		this.elapsedTime = elapsedTime;
	}

	public TiledMapTileLayer getCollisionLayer() {
		return collisionLayer;
	}

	public void setCollisionLayer(TiledMapTileLayer collisionLayer) {
		this.collisionLayer = collisionLayer;
	}

	public Vector2 getPosition() {
		return position;
	}

	public void setPosition(Vector2 position) {
		this.position = position;
	}

	public Vector2 getDimension() {
		return dimension;
	}

	public void setDimension(Vector2 dimension) {
		this.dimension = dimension;
	}

}
