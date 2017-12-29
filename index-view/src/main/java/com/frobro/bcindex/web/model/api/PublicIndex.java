package com.frobro.bcindex.web.model.api;

public enum PublicIndex {
  TEN_INDEX {
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.ODD_INDEX;
    }
  },
  TEN_EVEN_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.EVEN_INDEX;
    }
  },
  TWENTY_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.INDEX_TWENTY;
    }
  },
  TWENTY_EVEN_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.EVEN_TWENTY;
    }
  },
  FORTY_INDEX {
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.FORTY_INDEX;
    }
  },
  FORTY_EVEN_INDEX {
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.FORTY_EVEN_INDEX;
    }
  },
  ETHER_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.INDEX_ETH;
    }
  },
  EVEN_ETHER_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.EVEN_ETH;
    }
  },
  TOTAL_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.TOTAL_INDEX;
    }
  },
  TOTAL_EVEN_INDEX{
    @Override
    public IndexType getPrivateIdx() {
      return IndexType.TOTAL_EVEN_INDEX;
    }
  };


  public static PublicIndex getPublicIdx(IndexType privateIdx) {
    PublicIndex publicIndex;

    switch (privateIdx) {
      case EVEN_INDEX:
        publicIndex = TEN_EVEN_INDEX;
        break;
      case INDEX_TWENTY:
        publicIndex = TWENTY_INDEX;
        break;
      case EVEN_TWENTY:
        publicIndex = TWENTY_EVEN_INDEX;
        break;
      case INDEX_ETH:
        publicIndex = ETHER_INDEX;
        break;
      case EVEN_ETH:
        publicIndex = EVEN_ETHER_INDEX;
        break;
      case FORTY_INDEX:
        publicIndex = PublicIndex.FORTY_INDEX;
        break;
      case FORTY_EVEN_INDEX:
        publicIndex = PublicIndex.FORTY_EVEN_INDEX;
        break;
      case TOTAL_INDEX:
        publicIndex = PublicIndex.TOTAL_INDEX;
        break;
      case TOTAL_EVEN_INDEX:
        publicIndex = PublicIndex.TOTAL_EVEN_INDEX;
        break;
      default:
        publicIndex = TEN_INDEX;
        break;
    }
    return publicIndex;
  }

  abstract public IndexType getPrivateIdx();
}
