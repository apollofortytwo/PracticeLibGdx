package com.apollo.tile.screen;

import com.apollo.tile.entities.Player;
import com.badlogic.gdx.Gdx;
import com.badlogic.gdx.Screen;
import com.badlogic.gdx.graphics.GL20;
import com.badlogic.gdx.graphics.OrthographicCamera;
import com.badlogic.gdx.maps.tiled.TiledMap;
import com.badlogic.gdx.maps.tiled.TiledMapTileLayer;
import com.badlogic.gdx.maps.tiled.TmxMapLoader;
import com.badlogic.gdx.maps.tiled.renderers.OrthogonalTiledMapRenderer;

public class PlayScreen implements Screen {

	private TiledMap map;
	private OrthogonalTiledMapRenderer renderer;
	private OrthographicCamera camera;

	private Player player;

	@Override
	public void show() {
		TmxMapLoader loader = new TmxMapLoader();
		map = loader.load("Map.tmx");

		renderer = new OrthogonalTiledMapRenderer(map);

		camera = new OrthographicCamera();

		player = new Player((TiledMapTileLayer) (map.getLayers().get(0)));
		
	}

	@Override
	public void render(float delta) {
		Gdx.gl.glClearColor(0, 0, 0, 1);
		Gdx.gl.glClear(GL20.GL_COLOR_BUFFER_BIT);

		renderer.setView(camera);
		renderer.render();
		camera.update();

		renderer.getBatch().begin();

		player.render(renderer.getBatch());
		camera.position.x = player.getPosition().x;
		camera.position.y = player.getPosition().y;
		renderer.getBatch().end();
	}


	
	@Override
	public void resize(int width, int height) {
		camera.viewportHeight = height / 2;
		camera.viewportWidth = width / 2;

		camera.update();
	}

	@Override
	public void pause() {

	}

	@Override
	public void resume() {

	}

	@Override
	public void hide() {
		dispose();
	}

	@Override
	public void dispose() {
		map.dispose();
		renderer.dispose();

	}

	public TiledMap getMap() {
		return map;
	}

	public void setMap(TiledMap map) {
		this.map = map;
	}

	public OrthogonalTiledMapRenderer getRenderer() {
		return renderer;
	}

	public void setRenderer(OrthogonalTiledMapRenderer renderer) {
		this.renderer = renderer;
	}

	public OrthographicCamera getCamera() {
		return camera;
	}

	public void setCamera(OrthographicCamera camera) {
		this.camera = camera;
	}

	public Player getPlayer() {
		return player;
	}

	public void setPlayer(Player player) {
		this.player = player;
	}

}
