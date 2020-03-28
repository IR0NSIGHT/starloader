//
// Source code recreated from a .class file by IntelliJ IDEA
// (powered by Fernflower decompiler)
//

package org.schema.game.common.data.blockeffects.config;

import it.unimi.dsi.fastutil.objects.ObjectArrayList;
import java.io.DataInput;
import java.io.DataOutput;
import java.io.IOException;
import java.util.Iterator;
import java.util.List;
import java.util.Locale;
import java.util.Random;
import org.schema.common.util.data.DataUtil;
import org.schema.schine.network.SerialializationInterface;
import org.w3c.dom.Document;
import org.w3c.dom.Element;
import org.w3c.dom.Node;
import org.w3c.dom.NodeList;

public class ConfigGroup implements Comparable<ConfigGroup>, SerialializationInterface {
    public short ntId = -1;
    public String id;
    private static short ntIdGen = 1;
    public final List<EffectConfigElement> elements = new ObjectArrayList();
    private static final Object synch = new Object();

    public ConfigGroup() {
    }

    public ConfigGroup(String var1) {
        this.id = var1;
        this.assignNTID();
    }

    private void assignNTID() {
        synchronized(synch) {
            short var10001 = ntIdGen;
            ntIdGen = (short)(var10001 + 1);
            this.ntId = var10001;
        }
    }

    public Node write(Document var1) {
        Element var2;
        (var2 = var1.createElement("Group")).setAttribute("id", this.id);
        Iterator var3 = this.elements.iterator();

        while(var3.hasNext()) {
            Node var4 = ((EffectConfigElement)var3.next()).write(var1);
            var2.appendChild(var4);
        }

        return var2;
    }

    public long getHash() {
        long var1 = 0L + (long)(this.id.hashCode() * (this.elements.size() + 1));
        Random var3 = new Random(var1);

        for(int var4 = 0; var4 < this.elements.size(); ++var4) {
            EffectConfigElement var5 = (EffectConfigElement)this.elements.get(var4);
            var1 *= (long)DataUtil.primes[var3.nextInt(DataUtil.primes.length)] * var3.nextLong() * var5.addHash();
        }

        return var1;
    }

    public void parse(Node var1) throws IllegalArgumentException, IllegalAccessException {
        Node var2;
        if ((var2 = var1.getAttributes().getNamedItem("id")) == null) {
            throw new EffectException("No ID in " + var1.getNodeName());
        } else {
            this.id = var2.getNodeValue();
            this.assignNTID();
            NodeList var8 = var1.getChildNodes();

            for(int var3 = 0; var3 < var8.getLength(); ++var3) {
                Node var4;
                if ((var4 = var8.item(var3)).getNodeType() == 1) {
                    Node var5;
                    if ((var5 = var4.getAttributes().getNamedItem("type")) == null) {
                        throw new EffectException("No type in " + var1.getParentNode().getNodeName() + "->" + var1.getNodeName());
                    }

                    EffectConfigElement var6 = new EffectConfigElement();
                    String var9;
                    StatusEffectType var7 = StatusEffectType.valueOf((var9 = var5.getNodeValue()).trim().toUpperCase(Locale.ENGLISH));
                    if (var7 == null) {
                        throw new EffectException("Unknown type in " + var1.getParentNode().getNodeName() + "->" + var1.getNodeName() + ": '" + var9 + "', must be " + StatusEffectType.getAll());
                    }

                    var6.init(var7);
                    var6.readConfig(var4);
                    this.elements.add(var6);
                }
            }

        }
    }

    public void deserialize(DataInput var1, int var2, boolean var3) throws IOException {
        this.ntId = var1.readShort();

        assert this.ntId >= 0 : this.ntId;

        this.id = var1.readUTF();
        this.elements.clear();
        short var9 = var1.readShort();
        byte var10 = -1;

        try {
            for(int var4 = 0; var4 < var9; ++var4) {
                int var11 = var1.readByte() & 255;
                StatusEffectType var5 = StatusEffectType.values()[var11];
                EffectConfigElement var6;
                (var6 = new EffectConfigElement()).init(var5);
                var6.deserialize(var1);
                this.elements.add(var6);
            }

        } catch (RuntimeException var7) {
            System.err.println("'''''''''!!!!!!!!! Exception: size: " + var9 + "; last byte: " + var10 + "; " + this.ntId + "; " + this.id);
            var7.printStackTrace();
            throw var7;
        } catch (IOException var8) {
            System.err.println("'''''''''!!!!!!!!! Exception: size: " + var9 + "; last byte: " + var10 + "; " + this.ntId + "; " + this.id);
            var8.printStackTrace();
            throw var8;
        }
    }

    public void serialize(DataOutput var1, boolean var2) throws IOException {
        assert this.ntId >= 0 : this.ntId;

        assert this.id != null : this.id;

        var1.writeShort(this.ntId);
        var1.writeUTF(this.id);
        ObjectArrayList var4 = new ObjectArrayList(this.elements);
        var1.writeShort((short)var4.size());
        Iterator var5 = var4.iterator();

        while(var5.hasNext()) {
            EffectConfigElement var3 = (EffectConfigElement)var5.next();
            var1.writeByte((byte)var3.getType().ordinal());
            var3.serialize(var1);
        }

    }

    public int compareTo(ConfigGroup var1) {
        return this.id.compareTo(var1.id);
    }

    public int hashCode() {
        return this.id.toLowerCase(Locale.ENGLISH).hashCode();
    }

    public boolean equals(Object var1) {
        return this.id.toLowerCase(Locale.ENGLISH).equals(((ConfigGroup)var1).id.toLowerCase(Locale.ENGLISH));
    }

    public String toString() {
        return "ConfigGroup[" + this.id + "; " + this.ntId + "; (size: " + this.elements.size() + ")]";
    }

    public String getEffectDescription() {
        StringBuffer var1 = new StringBuffer();
        Iterator var2 = this.elements.iterator();

        while(var2.hasNext()) {
            String var3;
            if ((var3 = ((EffectConfigElement)var2.next()).getEffectDescription()).trim().length() > 0) {
                var1.append(var3 + "\n");
            }
        }

        return var1.toString();
    }
}
