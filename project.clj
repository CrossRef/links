(defproject links "0.1.0-SNAPSHOT"
  :description "FIXME: write description"
  :url "http://example.com/FIXME"
  :license {:name "Eclipse Public License"
            :url "http://www.eclipse.org/legal/epl-v10.html"}
  :dependencies [[org.clojure/clojure "1.6.0"]
                 [liberator "0.13"]
                 [compojure "1.3.4"]
                 [mysql-java "5.1.21"]
                 [korma "0.3.0"]
                 [http-kit "2.1.18"]
                 [lein-ring "0.9.3"]
                 [javax.servlet/servlet-api "2.5"]
                 [ring "1.3.0"]
                 [org.clojure/data.json "0.2.5"]
                 [clj-time "0.8.0"]
                 [org.clojure/tools.logging "0.3.0"]
                 [crossref-util "0.1.8"]]
  :main ^:skip-aot links.core
  :target-path "target/%s"
  :profiles {:uberjar {:aot :all}})
