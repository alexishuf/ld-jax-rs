package br.ufsc.inf.lapesd.ld_jaxrs.jena;

import java.util.ArrayList;
import java.util.HashSet;
import java.util.List;
import java.util.Set;

public class ContentTypesTestData {
    public static class Item {
        public final String resourcePath;
        public final String contentType;

        public Item(String resourcePath, String contentType) {
            this.resourcePath = resourcePath;
            this.contentType = contentType;
        }
    }

    public static List<Item> get() {
        Set<String> types = getContentTypes();
        String[] resources = {"Alan_Turing.n3"};

        List<Item> data = new ArrayList<>();
        for (String ttl : resources) {
            for (String contentType : types) data.add(new Item(ttl, contentType));
        }
        return data;
    }

    public static Set<String> getContentTypes() {
        Set<String> types = new HashSet<>(ModelMessageBodyWriter.getSupportedContentTypes());
        types.retainAll(ModelMessageBodyReader.getSupportedMediaTypes());
        return types;
    }
}
