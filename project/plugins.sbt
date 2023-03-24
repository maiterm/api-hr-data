addSbtPlugin("com.typesafe.play" % "sbt-plugin" % "2.8.19")
addSbtPlugin("org.foundweekends.giter8" % "sbt-giter8-scaffold" % "0.13.1")
libraryDependencies += "org.hsqldb" % "hsqldb" % "2.3.2"
libraryDependencies += "com.h2database"  %  "h2" % "1.4.200"
libraryDependencies +="com.microsoft.sqlserver" % "mssql-jdbc" % "9.4.0.jre11"
dependencyOverrides += "org.scala-lang.modules" %% "scala-parser-combinators" % "2.1.0"
addSbtPlugin("org.scalikejdbc" %% "scalikejdbc-mapper-generator" % "4.0.0")
