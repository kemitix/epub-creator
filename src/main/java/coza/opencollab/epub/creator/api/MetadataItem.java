package coza.opencollab.epub.creator.api;

import lombok.AccessLevel;
import lombok.AllArgsConstructor;

@AllArgsConstructor(access = AccessLevel.PRIVATE)
public class MetadataItem {

    private final String name; //e.g. dc:description
    private final String id;// optional
    private final String property;// optional
    private final String refines;// optional
    private final String value;// optional

    public String getName() {
        return name;
    }

    public boolean hasId() {
        return id != null;
    }

    public String getId() {
        return id;
    }

    public boolean hasProperty() {
        return property != null;
    }

    public String getProperty() {
        return property;
    }

    public boolean hasRefines() {
        return refines != null;
    }

    public String getRefines() {
        return refines;
    }

    public boolean hasValue() {
        return value != null;
    }

    public String getValue() {
        return value;
    }

    public static Builder builder() {
        return new Builder() {
            @Override
            public Stage1 name(String name) {
                return new Stage1() {
                    @Override
                    public Stage2 id(String id) {
                        return new Stage2() {
                            @Override
                            public Stage3 property(String property) {
                                return new Stage3() {
                                    @Override
                                    public Stage4 refines(String refines) {
                                        return new Stage4() {
                                            @Override
                                            public MetadataItem value(String value) {
                                                return new MetadataItem(name, id, property, refines, value);
                                            }

                                            @Override
                                            public MetadataItem build() {
                                                return new MetadataItem(name, id, property, refines, null);
                                            }
                                        };
                                    }

                                    @Override
                                    public MetadataItem value(String value) {
                                        return new MetadataItem(name, id, property, null, value);
                                    }

                                    @Override
                                    public MetadataItem build() {
                                        return new MetadataItem(name, id, property, null, null);
                                    }
                                };
                            }

                            @Override
                            public MetadataItem value(String value) {
                                return new MetadataItem(name, id, null, null, value);
                            }

                            @Override
                            public MetadataItem build() {
                                return new MetadataItem(name, id, null, null, null);
                            }
                        };
                    }

                    @Override
                    public Stage3 property(String property) {
                        return new Stage3() {
                            @Override
                            public Stage4 refines(String refines) {
                                return new Stage4() {
                                    @Override
                                    public MetadataItem value(String value) {
                                        return new MetadataItem(name, null, property, refines, value);
                                    }

                                    @Override
                                    public MetadataItem build() {
                                        return new MetadataItem(name, null, property, refines, null);
                                    }
                                };
                            }

                            @Override
                            public MetadataItem value(String value) {
                                return new MetadataItem(name, null, property, null, value);
                            }

                            @Override
                            public MetadataItem build() {
                                return new MetadataItem(name, null, property, null, null);
                            }
                        };
                    }

                    @Override
                    public MetadataItem value(String value) {
                        return new MetadataItem(name, null, null, null, value);
                    }

                    @Override
                    public MetadataItem build() {
                        return new MetadataItem(name, null, null, null, null);
                    }
                };
            }
       };
    }

    public interface Builder {
        Stage1 name(String name);
        interface Stage1 {
            Stage2 id(String id);
            Stage3 property(String property);
            MetadataItem value(String value);
            MetadataItem build();
        }
        interface Stage2 {
            Stage3 property(String property);
            MetadataItem value(String value);
            MetadataItem build();
        }
        interface Stage3 {
            Stage4 refines(String refines);
            MetadataItem value(String value);
            MetadataItem build();
        }
        interface Stage4 {
            MetadataItem value(String value);
            MetadataItem build();
        }
    }
}
