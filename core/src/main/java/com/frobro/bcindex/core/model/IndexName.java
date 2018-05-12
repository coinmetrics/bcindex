package com.frobro.bcindex.core.model;

import com.frobro.bcindex.core.exception.UnkownIndexException;

public enum IndexName {
  TEN {
    @Override
    public IndexName getEvenMatch() {
      return TEN_EVEN;
    }
  },
  TEN_EVEN {
    @Override
    public boolean isEven() {
      return Boolean.TRUE;
    }
    @Override
    public IndexName getEvenMatch() {
      return TEN;
    }
  },

  TWENTY {
    @Override
    public IndexName getEvenMatch() {
      return TWENTY_EVEN;
    }
  },
  TWENTY_EVEN {
    @Override
    public boolean isEven() {
      return Boolean.TRUE;
    }
    @Override
    public IndexName getEvenMatch() {
      return TWENTY;
    }
  },

  FORTY {
    @Override
    public IndexName getEvenMatch() {
      return FORTY_EVEN;
    }
  },
  FORTY_EVEN {
    @Override
    public boolean isEven() {
      return Boolean.TRUE;
    }
    @Override
    public IndexName getEvenMatch() {
      return FORTY;
    }
  },

  TOTAL {
    @Override
    public IndexName getEvenMatch() {
      return TOTAL_EVEN;
    }
  },
  TOTAL_EVEN {
    @Override
    public boolean isEven() {
      return Boolean.TRUE;
    }
    @Override
    public IndexName getEvenMatch() {
      return TOTAL;
    }
  },

  ETHEREUM {
    @Override
    public IndexName getEvenMatch() {
      return ETHEREUM_EVEN;
    }
  },
  ETHEREUM_EVEN {
    @Override
    public boolean isEven() {
      return Boolean.TRUE;
    }
    @Override
    public IndexName getEvenMatch() {
      return ETHEREUM;
    }
  },

  CURRENCY,
  PLATFORM,
  APPLICATION;

  public IndexName getEvenMatch() {
    return null;
  }

  public boolean hasEven() {
    return getEvenMatch() != null;
  }

  public boolean isEven() {
    return Boolean.FALSE;
  }

  public int getNumNonEvenIndex() {
    int numNonEvenIdx = 0;
    for (IndexName idx : values()) {
      if (not(idx.isEven())) {
        numNonEvenIdx++;
      }
    }
    return numNonEvenIdx;
  }

  private boolean not(boolean b) {
    return !b;
  }

  public static IndexName getIndex(String name) {
    for (IndexName idx : values()) {
      if (idx.name().equalsIgnoreCase(name)) {
        return idx;
      }
    }
    throw new UnkownIndexException("could not find index named: " + name);
  }
}
