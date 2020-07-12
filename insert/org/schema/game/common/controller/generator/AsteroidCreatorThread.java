//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.controller.generator;

import api.listener.events.controller.asteroid.AsteroidGenerateEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.ObjectArrayFIFOQueue;
import obfuscated.d;
import obfuscated.e;
import obfuscated.f;
import obfuscated.g;
import obfuscated.h;
import obfuscated.i;
import obfuscated.j;
import obfuscated.k;
import obfuscated.l;
import org.schema.common.util.linAlg.Vector3i;
import org.schema.game.common.controller.FloatingRock;
import org.schema.game.common.data.world.Segment;
import org.schema.game.common.data.world.SegmentData;
import org.schema.game.common.data.world.SegmentDataBitMap;
import org.schema.game.common.data.world.SegmentDataSingle;
import org.schema.game.server.controller.RequestData;
import org.schema.game.server.controller.RequestDataAsteroid;
import org.schema.game.server.controller.TerrainChunkCacheElement;
import org.schema.game.server.data.ServerConfig;
import org.schema.schine.common.language.Lng;
import org.schema.schine.common.language.Translatable;

public class AsteroidCreatorThread extends CreatorThread {
    public static final ObjectArrayFIFOQueue<RequestData> dataPool;
    private static final boolean OPTIMIZE_DATA = true;
    private d creator;

    public RequestData allocateRequestData(int var1, int var2, int var3) {
        synchronized(dataPool) {
            while(dataPool.isEmpty()) {
                try {
                    dataPool.wait();
                } catch (InterruptedException var4) {
                    var4.printStackTrace();
                }
            }

            return (RequestData)dataPool.dequeue();
        }
    }

    public void freeRequestData(RequestData var1, int var2, int var3, int var4) {
        assert var1 != null;

        var1.reset();
        synchronized(dataPool) {
            dataPool.enqueue(var1);
            dataPool.notify();
        }
    }

    public AsteroidCreatorThread(FloatingRock var1, AsteroidCreatorThread.AsteroidType var2) {
        super(var1);
        switch(var2) {
            case ICY:
                this.creator = new f(var1.getSeed());
                return;
            case LAVA:
                this.creator = new g(var1.getSeed());
                return;
            case MINERAL:
                this.creator = new h(var1.getSeed());
                return;
            case GOLDY:
                this.creator = new e(var1.getSeed());
                return;
            case ICE_CORE:
                this.creator = new i(var1.getSeed());
                return;
            case ROCKY_PURPLE:
                this.creator = new k(var1.getSeed());
                return;
            case ICE_HEAVY:
                this.creator = new j(var1.getSeed());
                return;
            case ROCKY:
            default:
                this.creator = new l(var1.getSeed());
        }
    }

    public int isConcurrent() {
        return 2;
    }

    public int loadFromDatabase(Segment var1) {
        return -1;
    }

    public void onNoExistingSegmentFound(Segment var1, RequestData requestData) {
        RequestDataAsteroid var5 = (RequestDataAsteroid)requestData;
        //INSERTED CODE @112
        AsteroidGenerateEvent event = new AsteroidGenerateEvent(this, requestData);
        StarLoader.fireEvent(event, true);
        ///
        this.creator.a(this.getSegmentController(), var1, var5);
        TerrainChunkCacheElement var6;
        if (!(var6 = var5.currentChunkCache).isEmpty()) {
            Object var3 = null;
            if (var6.isFullyFilledWithOneType()) {
                var3 = new SegmentDataSingle(false, var6.generationElementMap.getBlockDataFromList(0));
            } else if (var6.generationElementMap.containsBlockIndexList.size() <= 16) {
                int[] var7 = new int[var6.generationElementMap.containsBlockIndexList.size()];

                for(int var4 = 0; var4 < var7.length; ++var4) {
                    var7[var4] = var6.generationElementMap.getBlockDataFromList(var4);
                }

                var3 = new SegmentDataBitMap(false, var7, var1.getSegmentData());
            }

            if (var3 != null) {
                ((SegmentData)var3).setSize(var1.getSize());
                var1.getSegmentData().setBlockAddedForced(false);
                var1.getSegmentController().getSegmentProvider().getSegmentDataManager().addToFreeSegmentData(var1.getSegmentData(), true, true);
                ((SegmentData)var3).setBlockAddedForced(true);
                ((SegmentData)var3).assignData(var1);
            }

        }
    }

    public boolean predictEmpty(Vector3i var1) {
        return false;
    }

    static {
        dataPool = new ObjectArrayFIFOQueue((Integer)ServerConfig.CHUNK_REQUEST_THREAD_POOL_SIZE_CPU.getCurrentState());

        for(int var0 = 0; var0 < (Integer)ServerConfig.CHUNK_REQUEST_THREAD_POOL_SIZE_CPU.getCurrentState(); ++var0) {
            dataPool.enqueue(new RequestDataAsteroid());
        }

    }

    public static enum AsteroidType {
        ROCKY(0.65F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_0;
            }
        }),
        ICY(0.483F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_1;
            }
        }),
        LAVA(0.9F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_2;
            }
        }),
        MINERAL(0.566F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_3;
            }
        }),
        GOLDY(0.733F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_4;
            }
        }),
        ICE_CORE(0.483F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_5;
            }
        }),
        ROCKY_PURPLE(0.816F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_6;
            }
        }),
        ICE_HEAVY(0.4F, new Translatable() {
            public final String getName(Enum var1) {
                return Lng.ORG_SCHEMA_GAME_COMMON_CONTROLLER_GENERATOR_ASTEROIDCREATORTHREAD_7;
            }
        });

        private final Translatable translation;
        public final float temperature;

        private AsteroidType(float var3, Translatable var4) {
            this.temperature = var3;
            this.translation = var4;
        }

        public final String getTranslation() {
            return this.translation.getName(this);
        }
    }
}
