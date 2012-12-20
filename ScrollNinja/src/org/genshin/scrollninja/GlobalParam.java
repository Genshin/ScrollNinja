package org.genshin.scrollninja;

import java.io.IOException;

import org.genshin.engine.manager.RenderableManager;
import org.genshin.engine.manager.UpdatableManager;

import com.badlogic.gdx.Files.FileType;
import com.badlogic.gdx.files.FileHandle;
import com.badlogic.gdx.utils.XmlReader;
import com.badlogic.gdx.utils.XmlReader.Element;

/**
 * プロジェクト全体で共有するパラメータ
 * @author kou
 * @since		1.0
 * @version	1.0
 */
public enum GlobalParam
{
	/** シングルトンインスタンス */
	INSTANCE;
	
	/**
	 * コンストラクタ
	 */
	private GlobalParam()
	{
		final XmlReader xmlReader = new XmlReader();
		Element rootElement = null;
		try
		{
			final class InternalFileHandle extends FileHandle { InternalFileHandle(String fileName) { super(fileName, FileType.Internal); } }
			rootElement = xmlReader.parse(new InternalFileHandle("data/xml/global_param.xml"));
		}
		catch (IOException e)
		{
			e.printStackTrace();
		}
		
		//---- クライアント領域のサイズなど
		CLIENT_WIDTH = rootElement.getInt("ClientWidth", 640);
		CLIENT_HEIGHT = rootElement.getInt("ClientHeight", 480);
		FULLSCREEN = rootElement.getBoolean("Fullscreen", false);
		
		//---- 宇宙の法則
		// 世界の単位
		WORLD_SCALE = 128.0f / rootElement.getFloat("MaxWorldVelocity", 128.0f);
		INV_WORLD_SCALE = 1.0f / WORLD_SCALE;
		
		// 重力加速度
		GRAVITY = rootElement.getFloat("Gravity", 1000.0f) * -1.0f * WORLD_SCALE;
		
		//---- ディレクトリパス
		XML_DIRECTORY_PATH			= rootElement.get("XMLDirectoryPath");
		COLLISION_DIRECTORY_PATH	= rootElement.get("CollisionDirectoryPath");
		TEXTURE_DIRECTORY_PATH		= rootElement.get("TextureDirectoryPath");
		
		//---- XMLファイルのパス
		OBJECT_PARAM_XML_FILE_NAME		= rootElement.get("ObjectParamXMLFileName");
		COLLISION_PARAM_XML_FILE_NAME	= rootElement.get("CollisionParamXMLFileName");
		SPRITE_PARAM_XML_FILE_NAME		= rootElement.get("SpriteParamXMLFileName");
	}
	
	/** クライアント領域の横幅 */
	public final int CLIENT_WIDTH;

	/** クライアント領域の縦幅 */
	public final int CLIENT_HEIGHT;
	
	/** フルスクリーンフラグ */
	public final boolean FULLSCREEN;
	
	/** 世界の単位 */
	public final float WORLD_SCALE;
	
	/** 世界の単位を逆にしたもの（1/WORLD_SCALE） */
	public final float INV_WORLD_SCALE;
	
	/** 重力加速度（毎秒） */
	public final float GRAVITY;

	/** XMLファイルを格納しているディレクトリのパス */
	public final String XML_DIRECTORY_PATH;
	
	/** 衝突に関連するファイルを格納しているディレクトリのパス */
	public final String COLLISION_DIRECTORY_PATH;
	
	/** テクスチャを格納しているディレクトリのパス */
	public final String TEXTURE_DIRECTORY_PATH;
	
	/** オブジェクトパラメータを記述したXMLファイルのパス */
	public final String OBJECT_PARAM_XML_FILE_NAME;
	
	/** 衝突パラメータを記述したXMLファイルのパス */
	public final String COLLISION_PARAM_XML_FILE_NAME;
	
	/** スプライトパラメータを記述したXMLファイルのパス */
	public final String SPRITE_PARAM_XML_FILE_NAME;
	
	/** 現在指定されている更新処理を管理するオブジェクト */
	public UpdatableManager currentUpdatableManager = null;
	
	/** 現在指定されている描画処理を管理するオブジェクト */
	public RenderableManager currentRenderableManager = null;
	
	/** ゲーム内時間（秒） */
	public float gameTime = 0.0f;
	
	/** ゲーム内時間（フレーム数） */
	public int frameCount = 0;
}
