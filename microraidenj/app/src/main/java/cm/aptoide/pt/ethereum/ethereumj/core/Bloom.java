/*
 * Copyright (c) [2016] [ <ether.camp> ]
 * This file is part of the ethereumJ library.
 *
 * The ethereumJ library is free software: you can redistribute it and/or modify
 * it under the terms of the GNU Lesser General Public License as published by
 * the Free Software Foundation, either version 3 of the License, or
 * (at your option) any later version.
 *
 * The ethereumJ library is distributed in the hope that it will be useful,
 * but WITHOUT ANY WARRANTY; without even the implied warranty of
 * MERCHANTABILITY or FITNESS FOR A PARTICULAR PURPOSE. See the
 * GNU Lesser General Public License for more details.
 *
 * You should have received a copy of the GNU Lesser General Public License
 * along with the ethereumJ library. If not, see <http://www.gnu.org/licenses/>.
 */
package cm.aptoide.pt.ethereum.ethereumj.core;

import cm.aptoide.pt.ethereum.ethereumj.util.ByteUtil;
import java.util.Arrays;
import org.spongycastle.util.encoders.Hex;

/**
 * See http://www.herongyang.com/Java/Bit-String-Set-Bit-to-Byte-Array.html.
 *
 * @author Roman Mandeleil
 * @since 20.11.2014
 */

public class Bloom {

  static final int _8STEPS = 8;
  static final int _3LOW_BITS = 7;
  static final int ENSURE_BYTE = 255;

  byte[] data = new byte[256];

  public Bloom() {
  }

  public Bloom(byte[] data) {
    this.data = data;
  }

  public static cm.aptoide.pt.ethereum.ethereumj.core.Bloom create(byte[] toBloom) {

    int mov1 =
        (((toBloom[0] & ENSURE_BYTE) & (_3LOW_BITS)) << _8STEPS) + ((toBloom[1]) & ENSURE_BYTE);
    int mov2 =
        (((toBloom[2] & ENSURE_BYTE) & (_3LOW_BITS)) << _8STEPS) + ((toBloom[3]) & ENSURE_BYTE);
    int mov3 =
        (((toBloom[4] & ENSURE_BYTE) & (_3LOW_BITS)) << _8STEPS) + ((toBloom[5]) & ENSURE_BYTE);

    byte[] data = new byte[256];
    cm.aptoide.pt.ethereum.ethereumj.core.Bloom bloom =
        new cm.aptoide.pt.ethereum.ethereumj.core.Bloom(data);

    ByteUtil.setBit(data, mov1, 1);
    ByteUtil.setBit(data, mov2, 1);
    ByteUtil.setBit(data, mov3, 1);

    return bloom;
  }

  public void or(cm.aptoide.pt.ethereum.ethereumj.core.Bloom bloom) {
    for (int i = 0; i < data.length; ++i) {
      data[i] |= bloom.data[i];
    }
  }

  public boolean matches(cm.aptoide.pt.ethereum.ethereumj.core.Bloom topicBloom) {
    cm.aptoide.pt.ethereum.ethereumj.core.Bloom copy = copy();
    copy.or(topicBloom);
    return this.equals(copy);
  }

  public byte[] getData() {
    return data;
  }

  public cm.aptoide.pt.ethereum.ethereumj.core.Bloom copy() {
    return new cm.aptoide.pt.ethereum.ethereumj.core.Bloom(
        Arrays.copyOf(getData(), getData().length));
  }

  @Override public int hashCode() {
    return data != null ? Arrays.hashCode(data) : 0;
  }

  @Override public boolean equals(Object o) {
    if (this == o) return true;
    if (o == null || getClass() != o.getClass()) return false;

    cm.aptoide.pt.ethereum.ethereumj.core.Bloom bloom =
        (cm.aptoide.pt.ethereum.ethereumj.core.Bloom) o;

    return Arrays.equals(data, bloom.data);
  }

  @Override public String toString() {
    return Hex.toHexString(data);
  }
}
