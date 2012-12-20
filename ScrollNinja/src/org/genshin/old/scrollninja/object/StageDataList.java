package org.genshin.old.scrollninja.object;

import java.util.ArrayList;

import org.genshin.scrollninja.GlobalParam;

import com.badlogic.gdx.math.Vector2;

// TODO 別ファイルから読み込めるようにしたい
public class StageDataList {
	public StageDataList() {}

	public static StageData lead(int stageNum) {
		StageData data = new StageDataList().new StageData();

		if (stageNum == 0) {
			final float worldScale = GlobalParam.INSTANCE.WORLD_SCALE;
			
			// ステージ０設定
			data.size.set(3840.0f * worldScale, 2500.0f * worldScale);
			data.startPosition.set(data.size.x * 0.5f, data.size.y * 0.5f);
			
			data.collisionData.fileName = "data/stages/test.json";
			data.collisionData.bodyName = "bgTest";
			
//			{
//				StageData.EnemyData enemy = data.new EnemyData();
//				enemy.type = 0;
//				enemy.num = 1;
//				enemy.position.set( data.startPosition.x + 20.0f, data.startPosition.y );
//				data.enemyData.add(enemy);
//			}
//			
//			{
//				StageData.EnemyData enemy = data.new EnemyData();
//				enemy.type = 1;
//				enemy.num = 1;
//				enemy.position.set( data.startPosition.x + 30.0f, data.startPosition.y );
//				data.enemyData.add(enemy);
//			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 1920.0f / 3840.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_far_03.png";
				bg.size.set(4096.0f * worldScale, 4096.0f * worldScale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 2560.0f / 3840.0f;
				final float scale = 3840.0f / 2560.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_far_02.png";
				bg.size.set(4096.0f * worldScale * scale, 2048.0f * worldScale * scale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 2700.0f / 3840.0f;
				final float scale = 3840.0f / 2700.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_far_01.png";
				bg.size.set(4096.0f * worldScale * scale, 2048.0f * worldScale * scale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 1.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_near_01.png";
				bg.size.set(4096.0f * worldScale, 4096.0f * worldScale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 1.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_main.png";
				bg.size.set(4096.0f * worldScale, 4096.0f * worldScale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 3920.0f / 3840.0f;
				final float scale = 3840.0f / 3920.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_near_02.png";
				bg.size.set(4096.0f * worldScale * scale, 4096.0f * worldScale * scale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 2.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_light_01.png";
				bg.size.set(4096.0f * worldScale, 4096.0f * worldScale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 4.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_near_03.png";
				bg.size.set(4096.0f * worldScale, 512.0f * worldScale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
			
			{
				StageData.LayerData layer = data.new LayerData();
				layer.scale = 4096.0f / 3840.0f * 4.0f;
				
				StageData.LayerData.BGData bg = layer.new BGData();
				bg.textureFileName = "data/textures/stage/test_light_02.png";
				bg.size.set(4096.0f * worldScale, 4096.0f * worldScale);
				bg.position.set(0.0f * worldScale, 0.0f * worldScale);
				layer.bgData.add(bg);
				
				data.layerData.add(layer);
			}
		}

		// ステージ１設定
		// data = new StageDataList().new StageData();

		return data;
	}

	class StageData {
		public final Vector2 size = new Vector2();
		public final Vector2 startPosition = new Vector2();
		
		public final CollisionData collisionData = new CollisionData();
		
		public final ArrayList<EnemyData> enemyData = new ArrayList<EnemyData>(1);
		
		public final ArrayList<LayerData> layerData = new ArrayList<LayerData>(1);
		
		
		class CollisionData
		{
			public String fileName = "";
			public String bodyName = "";
		}
		
		class EnemyData
		{
			public int type;
			public int num;
			public final Vector2 position = new Vector2();
		}
		
		class LayerData
		{
			public float scale;
			public final ArrayList<BGData> bgData = new ArrayList<BGData>(1);
			
			class BGData
			{
				public String textureFileName = "";
				public final Vector2 size = new Vector2();
				public final Vector2 position = new Vector2();
			}
		}
	}
}