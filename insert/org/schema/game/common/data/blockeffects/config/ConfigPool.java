//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.blockeffects.config;

import api.listener.events.register.RegisterEffectsEvent;
import api.mod.StarLoader;
import it.unimi.dsi.fastutil.objects.Object2ObjectOpenHashMap;
import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import it.unimi.dsi.fastutil.shorts.Short2ObjectMap;
import it.unimi.dsi.fastutil.shorts.Short2ObjectOpenHashMap;
import java.io.BufferedInputStream;
import java.io.File;
import java.io.FileInputStream;
import java.io.FileNotFoundException;
import java.io.IOException;
import java.io.StringWriter;
import java.util.Collections;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Map;
import javax.xml.parsers.DocumentBuilderFactory;
import javax.xml.parsers.ParserConfigurationException;
import javax.xml.transform.Transformer;
import javax.xml.transform.TransformerException;
import javax.xml.transform.TransformerFactory;
import javax.xml.transform.dom.DOMSource;
import javax.xml.transform.stream.StreamResult;

import org.apache.commons.lang3.tuple.Pair;
import org.schema.common.XMLTools;
import org.schema.common.util.data.DataUtil;
import org.schema.game.client.controller.ClientChannel;
import org.schema.game.client.data.ClientStatics;
import org.schema.game.client.view.GameResourceLoader;
import org.schema.game.common.data.blockeffects.config.elements.ModifierStackType;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectBooleanValue;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectFloatValue;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectParameterNames;
import org.schema.game.common.data.blockeffects.config.parameter.StatusEffectParameterType;
import org.schema.game.network.objects.remote.RemoteEffectConfigGroup;
import org.schema.game.server.data.GameServerState;
import org.schema.game.server.data.ServerExecutionJob;
import org.schema.schine.network.objects.remote.RemoteString;
import org.schema.schine.resource.FileExt;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;
import org.xml.sax.SAXException;


public class ConfigPool {
    public String hash = "";
    public static final String CONFIG_FILENAME = "EffectConfig.xml";
    public static final String configPathTemplate;
    public static final String configPathHOWTO;
    public final Short2ObjectMap<ConfigGroup> ntMap = new Short2ObjectOpenHashMap();
    public final Map<String, ConfigGroup> poolMapLowerCase = new Object2ObjectOpenHashMap();
    public final List<ConfigGroup> pool = new ObjectArrayList();
    private String hashRequestedByClient;

    public ConfigPool() {
    }

    public File getPath(boolean var1) {
        File var2;
        if (var1) {
            if (!(var2 = new File(GameServerState.DATABASE_PATH + "EffectConfig.xml")).exists()) {
                var2 = new File(DataUtil.dataPath + File.separator + "config" + File.separator + "EffectConfig.xml");
            }
        } else if (!(var2 = new File(ClientStatics.ENTITY_DATABASE_PATH + "EffectConfig.xml")).exists()) {
            var2 = new File(DataUtil.dataPath + File.separator + "config" + File.separator + "EffectConfig.xml");
        }

        return var2;
    }

    public void readConfigFromFile(File var1) throws FileNotFoundException, SAXException, IOException, ParserConfigurationException, IllegalArgumentException, IllegalAccessException {
        if (!var1.exists()) {
            System.err.println("[CONFIGPOOL] no effect config found: " + var1.getAbsolutePath());
            throw new FileNotFoundException("Not found " + var1.getAbsolutePath());
        } else {
            Document var7 = DocumentBuilderFactory.newInstance().newDocumentBuilder().parse(new BufferedInputStream(new FileInputStream(var1), 4096));
            boolean var2 = false;
            FileExt var3;
            if ((var3 = new FileExt(GameResourceLoader.CUSTOM_EFFECT_CONFIG_PATH + "customEffectConfig.xml")).exists()) {
                Document var10 = XMLTools.loadXML(var3);
                System.err.println("[SERVER] Custom effect config found");
                XMLTools.mergeDocumentOnAttrib(var7, var10, "id");

                try {
                    XMLTools.writeDocument(new FileExt("EffectConfigMergeResult.xml"), var7);
                    new FileExt("EffectConfigMergeResult.xml");
                    var2 = true;
                } catch (TransformerException var6) {
                    var6.printStackTrace();
                }
            }

            NodeList var11 = var7.getDocumentElement().getChildNodes();
            String var8 = var7.getDocumentElement().getAttribute("hash");
            if (!var2 && var8 != null) {
                this.hash = var8;
            }

            for(int var9 = 0; var9 < var11.getLength(); ++var9) {
                Node var4;
                if ((var4 = var11.item(var9)).getNodeType() == 1) {
                    ConfigGroup var5;
                    (var5 = new ConfigGroup()).parse(var4);
                    this.add(var5);
                }
            }
            //INSERTED CODE @120
            //Event:

            RegisterEffectsEvent event = new RegisterEffectsEvent();
            StarLoader.fireEvent(RegisterEffectsEvent.class, event, true);

            for (Pair<StatusEffectType, Float> effect : event.registeredEffects) {
                ConfigGroup group = new ConfigGroup(effect.getLeft().name());

                //BOOLEAN EFFECT - If an effect is registered with a float value it will also have a boolean value
                /*EffectConfigElement bElement = new EffectConfigElement();
                StatusEffectType bType = effect.getLeft();
                bElement.init(bType);
                bElement.value = new StatusEffectBooleanValue();
                StatusEffectBooleanValue bValue = (StatusEffectBooleanValue) bElement.value;
                bValue.value.set(true);
                bElement.priority = 1;*/

                //FLOAT EFFECT
                EffectConfigElement element = new EffectConfigElement();
                StatusEffectType type = effect.getLeft();
                element.init(type);
                element.value = new StatusEffectFloatValue();
                StatusEffectFloatValue value = (StatusEffectFloatValue) element.value;
                value.value.set(10F);
                element.priority = 1;
                //group.elements.add(element);


                //ADD

                group.elements.add(element);

                this.add(group);
            }
////////////////////
            /*ConfigGroup group = new ConfigGroup("bruh");
            EffectConfigElement element = new EffectConfigElement();
            StatusEffectType type = StatusEffectType.CUSTOM_EFFECT_01;
            element.init(type);
            element.value = new StatusEffectFloatValue();
            StatusEffectFloatValue value = (StatusEffectFloatValue) element.value;
            value.value.set(100);
            element.priority = 1;
            group.elements.add(element);
            this.add(group);*/
            ///

            if (var2) {
                this.hash = this.calculateHash();
            }

        }
    }

    public void clientReceive(ClientChannel var1) {
        assert !var1.isOnServer();

        this.receiveConfigGroups(var1);
        Iterator var2 = var1.getNetworkObject().effectConfigSig.getReceiveBuffer().iterator();

        while(var2.hasNext()) {
            RemoteString var3 = (RemoteString)var2.next();
            if (!var1.isOnServer()) {
                this.hash = (String)var3.get();
                System.err.println("[CONFIGPOOL] CLIENT RECEIVED HASH: " + this.hash);
            }
        }

    }

    public String calculateHash() {
        ObjectArrayList var1;
        Collections.sort(var1 = new ObjectArrayList(this.pool));
        long var2 = 0L;

        for(int var4 = 0; var4 < var1.size(); ++var4) {
            var2 += (long)DataUtil.primes[var4 % DataUtil.primes.length] * ((ConfigGroup)var1.get(var4)).getHash();
        }

        return String.valueOf(var2);
    }

    public void remove(ConfigGroup var1) {
        assert var1 != null;

        this.pool.remove(var1);
        this.poolMapLowerCase.remove(var1.id.toLowerCase(Locale.ENGLISH));
        this.ntMap.remove(var1.ntId);
    }

    public void add(ConfigGroup var1) {
        assert var1 != null;

        this.pool.add(var1);
        this.poolMapLowerCase.put(var1.id.toLowerCase(Locale.ENGLISH), var1);

        assert var1.ntId > 0;

        this.ntMap.put(var1.ntId, var1);
    }

    public void clear() {
        this.pool.clear();
        this.poolMapLowerCase.clear();
        this.ntMap.clear();
    }

    public void write(File var1) throws Exception {
        Document var2;
        Element var3 = (var2 = DocumentBuilderFactory.newInstance().newDocumentBuilder().newDocument()).createElement("Config");
        var2.appendChild(var3);
        var2.setXmlVersion("1.0");
        this.hash = this.calculateHash();
        Iterator var4 = this.pool.iterator();

        while(var4.hasNext()) {
            Node var5 = ((ConfigGroup)var4.next()).write(var2);
            var3.appendChild(var5);
        }

        var3.setAttribute("hash", this.hash);
        Transformer var6;
        (var6 = TransformerFactory.newInstance().newTransformer()).setOutputProperty("omit-xml-declaration", "yes");
        var6.setOutputProperty("indent", "yes");
        var6.setOutputProperty("{http://xml.apache.org/xslt}indent-amount", "4");
        new StringWriter();
        StreamResult var7 = new StreamResult(var1);
        DOMSource var8 = new DOMSource(var2);
        var6.transform(var8, var7);
    }

    public void updateToNetworkObject(ClientChannel var1) {
    }

    public void requestConfig(ClientChannel var1) {
        var1.getNetworkObject().effectConfigSig.add(new RemoteString(this.hash, var1.getNetworkObject()));
    }

    public void checkRequestReceived(final ClientChannel var1) {
        assert var1.isOnServer();

        Iterator var2 = var1.getNetworkObject().effectConfigSig.getReceiveBuffer().iterator();

        while(true) {
            RemoteString var3;
            do {
                if (!var2.hasNext()) {
                    return;
                }
            } while(!((String)(var3 = (RemoteString)var2.next()).get()).trim().isEmpty() && this.hash.equals(var3.get()));

            ServerExecutionJob var4 = new ServerExecutionJob() {
                public boolean execute(GameServerState var1x) {
                    var1.getNetworkObject().effectConfigSig.add(new RemoteString(ConfigPool.this.hash, var1.isOnServer()));
                    ConfigPool.this.sendAllConfigGroups(var1);
                    return true;
                }
            };
            ((GameServerState)var1.getState()).getServerExecutionJobs().enqueue(var4);
        }
    }

    private void sendConfigGroup(ClientChannel var1, ConfigGroup var2) {
        var1.getNetworkObject().effectConfigGroupBuffer.add(new RemoteEffectConfigGroup(var2, var1.isOnServer()));
    }

    private void sendAllConfigGroups(ClientChannel var1) {
        Iterator var2 = this.pool.iterator();

        while(var2.hasNext()) {
            ConfigGroup var3 = (ConfigGroup)var2.next();
            this.sendConfigGroup(var1, var3);
        }

    }

    private void receiveConfigGroups(ClientChannel var1) {
        ObjectArrayList var3;
        if ((var3 = var1.getNetworkObject().effectConfigGroupBuffer.getReceiveBuffer()).size() > 0) {
            System.err.println("[CONFIGPOOL] received new config groups: " + var3.size());
            this.clear();
            Iterator var4 = var3.iterator();

            while(var4.hasNext()) {
                ConfigGroup var2 = (ConfigGroup)((RemoteEffectConfigGroup)var4.next()).get();
                this.add(var2);
            }
        }

    }

    public void checkClientRequest(ClientChannel var1) {
        if (this.hashRequestedByClient == null || !this.hashRequestedByClient.equals(this.hash)) {
            this.requestConfig(var1);
            this.hashRequestedByClient = this.hash;
        }

    }

    static {
        configPathTemplate = "." + File.separator + "data" + File.separator + "config" + File.separator + "customConfigTemplate" + File.separator + "customEffectConfigTemplate.xml";
        configPathHOWTO = "." + File.separator + "data" + File.separator + "config" + File.separator + "customConfigTemplate" + File.separator + "CustomEffectConfigHowto.txt";
    }
}
